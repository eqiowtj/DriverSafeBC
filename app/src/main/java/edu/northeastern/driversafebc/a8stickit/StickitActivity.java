package edu.northeastern.driversafebc.a8stickit;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.northeastern.driversafebc.R;
import edu.northeastern.driversafebc.databinding.ActivityStickitBinding;

public class StickitActivity extends AppCompatActivity {

    private static final int STICKER_ID_MIN = 1;
    private static final int STICKER_ID_MAX = 10;
    private ActivityStickitBinding binding;
    private DatabaseReference databaseReference;
    private ChildEventListener newStickerEventListener;

    private Map<Integer, TextView> stickerNumberSentTextViews;

    private String loggedInUser;
    private int selectedStickerId;
    private String selectedReceiver;
    private Map<Integer, Integer> sentCount;
    private List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStickitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sentCount = new HashMap<>();
        users = new ArrayList<>();
        createNotificationChannel();

        binding.editTextUsername.addTextChangedListener(new TextValidateListener(() -> {
            boolean isUsernameValid = binding.editTextUsername.getText().toString().length() > 0;
            binding.buttonLogin.setEnabled(isUsernameValid);
        }));

        if (savedInstanceState != null) {
            loggedInUser = savedInstanceState.getString("loggedInUser");
            if (loggedInUser != null) {
                login(loggedInUser);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("loggedInUser", loggedInUser);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (newStickerEventListener != null) {
            databaseReference.child("inbox").child(loggedInUser).removeEventListener(newStickerEventListener);
        }
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id), "inbox", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("inbox");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void sendNotification(String sender, int stickerId) {
        Intent intent = new Intent(this, getClass());
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Instant.now().toEpochMilli(), intent, FLAG_IMMUTABLE);
        String imageName = STICKER_ID_MIN <= stickerId && stickerId <= STICKER_ID_MAX ? "sticker_" + stickerId : "sticker_unknown";
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(sender + " sent you a sticker")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(imageName, "drawable", getPackageName())))
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify((int) Instant.now().toEpochMilli(), notification);
    }

    private void stickerReceived(String sender, int stickerId, Instant ts) {
        View item = LayoutInflater.from(this).inflate(R.layout.sticker_item, binding.linearLayoutStickit, false);
        ImageView imageView = item.findViewById(R.id.stickerImage);
        TextView textViewFrom = item.findViewById(R.id.textViewFrom);
        TextView textViewTime = item.findViewById(R.id.textViewTime);
        textViewFrom.setText(sender);
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        textViewTime.setText(formatter.format(ts));
        String imageName = STICKER_ID_MIN <= stickerId && stickerId <= STICKER_ID_MAX ? "sticker_" + stickerId : "sticker_unknown";
        imageView.setImageResource(getResources()
                    .getIdentifier(imageName, "drawable", getPackageName()));
        binding.linearLayoutStickit.addView(item, 2);
        binding.textViewInboxEmptyLabel.setVisibility(View.GONE);
    }

    private void initialize() {
        loadUsers();
        populateStickers();
        loadSentCount();
        Instant initializedTime = Instant.now();
        newStickerEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String from = snapshot.child("from").getValue(String.class);
                Integer stickerId = snapshot.child("stickerId").getValue(Integer.class);
                Instant ts = Instant.ofEpochSecond(snapshot.child("ts").getValue(Long.class));
                stickerReceived(from, stickerId, ts);
                if (ts.isAfter(initializedTime)) {
                    sendNotification(from, stickerId);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        databaseReference.child("inbox").child(loggedInUser).addChildEventListener(newStickerEventListener);
    }


    private void loadUsers() {

        databaseReference.child("users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.err.println("Error");
            } else {
                Set<String> allUsers = ((Map<String, Object>) task.getResult().getValue()).keySet();
                for (String user : allUsers) {
                    if (!user.equals(loggedInUser)) {
                        users.add(user);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, users);
                binding.spinnerSendTo.setAdapter(adapter);
                binding.spinnerSendTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedReceiver = (String) adapterView.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }
        });
    }

    private void loadSentCount() {
        for (int i = STICKER_ID_MIN; i <= STICKER_ID_MAX; i++) {
            int finalI = i;
            databaseReference
                    .child("count")
                    .child(loggedInUser)
                    .child(String.valueOf(i))
                    .get().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            System.err.println("Error");
                        } else {
                            Integer value = task.getResult().getValue(Integer.class);
                            value = value == null ? 0 : value;
                            sentCount.put(finalI, value);
                            updateStickerNumberSentText(finalI, value);
                        }
                    });
        }
    }

    private void updateStickerNumberSentText(int stickerId, int count) {
        stickerNumberSentTextViews.get(stickerId).setText(getResources().getString(R.string.sent_indicator_text) + count);
    }


    private void populateStickers() {
        List<LinearLayout> stickerChoiceContainers = new ArrayList<>();
        stickerNumberSentTextViews = new HashMap<>();
        for (int i = STICKER_ID_MIN; i <= STICKER_ID_MAX; i++) {
            LinearLayout stickerChoiceContainer = new LinearLayout(this);
            stickerChoiceContainer.setOrientation(LinearLayout.VERTICAL);
            stickerChoiceContainer.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(60), dpToPx(80)));

            ImageView stickerImageView = new ImageView(this);
            stickerImageView.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(60), dpToPx(60)));
            stickerImageView.setImageResource(getResources()
                    .getIdentifier("sticker_" + i, "drawable", getPackageName()));

            TextView numberSentTextView = new TextView(this);
            numberSentTextView.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(60), dpToPx(20)));
            numberSentTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            numberSentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            stickerChoiceContainer.addView(stickerImageView);
            stickerChoiceContainer.addView(numberSentTextView);
            stickerChoiceContainer.setId(View.generateViewId());

            int finalI = i;
            stickerImageView.setOnClickListener(view -> {
                for (LinearLayout container : stickerChoiceContainers) {
                    container.setBackgroundColor(Color.WHITE);
                }
                stickerChoiceContainer.setBackgroundColor(getColor(R.color.sticker_selected));
                selectedStickerId = finalI;
                binding.buttonSendSticker.setEnabled(true);
            });

            binding.controlsContainer.addView(stickerChoiceContainer);
            binding.chooseStickerFlow.addView(stickerChoiceContainer);
            stickerChoiceContainers.add(stickerChoiceContainer);
            stickerNumberSentTextViews.put(i, numberSentTextView);
        }
    }

    public void loginClicked(View view) {
        login(binding.editTextUsername.getText().toString());
    }

    private void login(String username) {
        loggedInUser = username;
        binding.editTextUsername.setEnabled(false);
        binding.loginContainer.setVisibility(View.GONE);
        binding.controlsContainer.setVisibility(View.VISIBLE);
        binding.textViewCurrentUser.setText(loggedInUser);
        databaseReference.child("users").child(loggedInUser).setValue(true);
        initialize();
    }

    public void sendClicked(View view) {
        sendSticker(selectedReceiver, selectedStickerId);
    }

    private void sendSticker(String receiver, int stickerId) {
        System.out.println("Sending " + stickerId + " to " + receiver);

        sentCount.put(stickerId, sentCount.get(stickerId) + 1);
        updateStickerNumberSentText(stickerId, sentCount.get(stickerId));

        databaseReference
                .child("count")
                .child(loggedInUser)
                .child(String.valueOf(stickerId)).setValue(sentCount.get(stickerId));

        Map<String, Object> payload = new HashMap<>();
        payload.put("from", loggedInUser);
        payload.put("stickerId", stickerId);
        payload.put("ts", Instant.now().getEpochSecond());

        databaseReference
                .child("inbox")
                .child(receiver)
                .push().setValue(payload);

    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}
package edu.northeastern.driversafebc.a7atyourservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.driversafebc.a7atyourservice.pojo.Trivia;
import edu.northeastern.driversafebc.a7atyourservice.service.OpenTriviaService;
import edu.northeastern.driversafebc.databinding.ActivityAtYourServiceBinding;

public class AtYourServiceActivity extends AppCompatActivity {

    private ActivityAtYourServiceBinding binding;
    private OpenTriviaService openTriviaService;
    Handler uiHandler;
    RadioButton difficultyRadioButton, typeRadioButton;
    RadioGroup radioGroup;
    RadioGroup typeRadioGroup;
    String difficulty = "easy";
    String type = "boolean";
    int number = 10;

    private Handler uiHandler;
    private TriviaItemAdapter triviaItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAtYourServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openTriviaService = new OpenTriviaService();
        uiHandler = new Handler();

        radioGroup = binding.radioGroup;
        typeRadioGroup = binding.typeRadioGroup;


        binding.recyclerViewTriviaList.setLayoutManager(new LinearLayoutManager(this));
        triviaItemAdapter = new TriviaItemAdapter(this);
        binding.recyclerViewTriviaList.setAdapter(triviaItemAdapter);
    }

    public void checkButton(View view){
        EditText editText = binding.questionNumberEditText;
        String temp = editText.getText().toString();
        number = Integer.parseInt(temp);
        try{
            System.out.println(number);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        int radioDifficultyID = radioGroup.getCheckedRadioButtonId();
        int radioTypeID = typeRadioGroup.getCheckedRadioButtonId();
        difficultyRadioButton = findViewById(radioDifficultyID);
        typeRadioButton = findViewById(radioTypeID);


    }
    public void getTriviaButtonClicked(View view) {

        checkButton(view);
        if(difficultyRadioButton == (binding.difficultyEasyButton)){
            difficulty = "easy";
        }else if (difficultyRadioButton == binding.difficultyMeddumButton){
            difficulty = "medium";
        }else if (difficultyRadioButton == binding.difficultyHardButton){
            difficulty = "hard";
        }else if (difficultyRadioButton == binding.difficultyAnyButton){
            difficulty = " ";
        }
        if (typeRadioButton == binding.typeMultiple ){
            type = "multiple";
        }else if (typeRadioButton == binding.typeTruefalse){
            type = "boolean";
        }else if (typeRadioButton == binding.typeAny){
            type = " ";
        }


        openTriviaService.getTrivia(number,  difficulty, type, triviaResponse -> {
        triviaItemAdapter.clearTriviaList();
        openTriviaService.getTrivia(10, "easy", "multiple", triviaResponse -> {
            List<Trivia> triviaList = new ArrayList<>();
            for (Trivia trivia : triviaResponse.getResults()) {
                triviaList.add(trivia.initialize());
            }
            uiHandler.post(() -> {
                binding.textView.setText("");
                for (Trivia t : triviaResponse.results) {
                    binding.textView.append(t.question + "\n");
                }
            });
        });
    }

}
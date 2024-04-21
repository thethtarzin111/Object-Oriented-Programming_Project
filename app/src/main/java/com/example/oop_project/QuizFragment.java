package com.example.oop_project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizFragment extends Fragment implements View.OnClickListener{

    TextView totalQuestionsTextView;
    TextView questionTextView;
    TextView getTotalQuestionsTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;

    int score = 0;
    int totalQuestion = QA_Quiz.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        totalQuestionsTextView = view.findViewById(R.id.total_questions);
        questionTextView = view.findViewById(R.id.question);
        ansA = view.findViewById(R.id.ans_A);
        ansB = view.findViewById(R.id.ans_B);
        ansC = view.findViewById(R.id.ans_C);
        ansD = view.findViewById(R.id.ans_D);
        submitBtn = view.findViewById(R.id.submit_button);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        // Inflate the layout for this fragment

        totalQuestionsTextView.setText("Total questions: " + totalQuestion);

        loadNewQuestion();
        return view;

    }


    @Override
    public void onClick(View v) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);


        Button clickedButton = (Button) v;
        if(clickedButton.getId()==R.id.submit_button) {
            if(selectedAnswer.isEmpty()) {
                Toast.makeText(getActivity(), "Please choose an answer", Toast.LENGTH_LONG).show();
                return;
            }else {

                if (selectedAnswer.equals(QA_Quiz.correctAnswers[currentQuestionIndex])) {
                    score++;
                }
                currentQuestionIndex++;
                loadNewQuestion();

            }
        } else {
            //choices button clicked
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.DKGRAY);
        }
    }

    void loadNewQuestion() {

        if(currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }
        questionTextView.setText(QA_Quiz.question[currentQuestionIndex]);
        ansA.setText(QA_Quiz.choices[currentQuestionIndex][0]);
        ansB.setText(QA_Quiz.choices[currentQuestionIndex][1]);
        ansC.setText(QA_Quiz.choices[currentQuestionIndex][2]);
        ansD.setText(QA_Quiz.choices[currentQuestionIndex][3]);
    }
    void finishQuiz() {
        String passStatus = "";
        if(score>totalQuestion*0.6) {
            passStatus = "Passed";
        } else {
            passStatus = "Failed";
        }
        new AlertDialog.Builder(getContext())
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestion)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        restartQuiz();
                    }
                })
                .setCancelable(false)
                .show();

    }

    void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }
}

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.PreferenceResult;
import model.PreferenceTest;
import view.TestView;

public class TestController {

    private final PreferenceTest testModel;
    private final PreferenceResult resultModel;
    private final TestView view;

    private int currentQuestionIndex;

    // 사용자가 선택한 답 저장
    private int[] answers;

    public TestController(
            PreferenceTest testModel,
            PreferenceResult resultModel,
            TestView view
    ) {

        this.testModel = testModel;
        this.resultModel = resultModel;
        this.view = view;

        currentQuestionIndex = 0;

        answers = new int[testModel.getQuestionCount()];

        for (int i = 0; i < answers.length; i++) {
            answers[i] = -1;
        }

        initController();
    }

    private void initController() {

        view.addOptionButtonListener(
                new OptionButtonClickListener()
        );

        view.addResultButtonListener(
                new ResultButtonClickListener()
        );

        view.addBackButtonListener(
                new BackButtonClickListener()
        );

        updateView();
    }

    private void updateView() {

        if (currentQuestionIndex
                < testModel.getQuestionCount()) {

            PreferenceTest.Question question =
                    testModel.getQuestions()
                            .get(currentQuestionIndex);

            view.updateProgress(
                    currentQuestionIndex + 1,
                    testModel.getQuestionCount()
            );

            view.showQuestion(
                    question.getQuery(),
                    question.getOptions()
            );

        } else {

            view.showCompletionView();
        }
    }

    private void handleOptionSelected(
            int selectedOptionIndex
    ) {

        if (selectedOptionIndex < 0) {
            return;
        }

        // 답 저장
        answers[currentQuestionIndex]
                = selectedOptionIndex;

        currentQuestionIndex++;

        if (currentQuestionIndex
                >= testModel.getQuestionCount()) {

            recalculateResult();
        }

        updateView();
    }

    private void recalculateResult() {

        resultModel.clear();

        for (int i = 0; i < answers.length; i++) {

            if (answers[i] != -1) {

                resultModel.accumulateScore(
                        i,
                        answers[i]
                );
            }
        }

        resultModel.finalizeResult();
    }

    private void goPreviousQuestion() {

        if (currentQuestionIndex > 0) {

            currentQuestionIndex--;

            updateView();
        }
    }

    public void resetTest() {

        currentQuestionIndex = 0;

        resultModel.clear();

        for (int i = 0; i < answers.length; i++) {
            answers[i] = -1;
        }

        updateView();
    }

    private class OptionButtonClickListener
            implements ActionListener {

        @Override
        public void actionPerformed(
                ActionEvent e
        ) {

            JButton button =
                    (JButton) e.getSource();

            handleOptionSelected(
                    view.getOptionButtonIndex(button)
            );
        }
    }

    private class ResultButtonClickListener
            implements ActionListener {

        @Override
        public void actionPerformed(
                ActionEvent e
        ) {

            view.navigateToRecommendResult();
        }
    }

    private class BackButtonClickListener
            implements ActionListener {

        @Override
        public void actionPerformed(
                ActionEvent e
        ) {

            goPreviousQuestion();
        }
    }
}
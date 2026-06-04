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

    public TestController(PreferenceTest testModel, PreferenceResult resultModel, TestView view) {
        this.testModel = testModel;
        this.resultModel = resultModel;
        this.view = view;
        this.currentQuestionIndex = 0;

        initController();
    }

    // 리스너 등록 및 첫 화면 표시
    private void initController() {
        view.addOptionButtonListener(new OptionButtonClickListener());
        updateView();
    }

    // 현재 문항에 맞게 화면 갱신
    private void updateView() {
        if (currentQuestionIndex < testModel.getQuestionCount()) {
            PreferenceTest.Question currentQuestion =
                    testModel.getQuestions().get(currentQuestionIndex);

            view.showQuestion(
                    currentQuestion.getQuery(),
                    currentQuestion.getOptions());
        } else {
            view.navigateToRecommendResult();
        }
    }

    // 선택지 클릭 처리
    private void handleOptionSelected(int selectedOptionIndex) {
        if (selectedOptionIndex < 0 ||
            currentQuestionIndex >= testModel.getQuestionCount()) {
            return;
        }

        resultModel.accumulateScore(currentQuestionIndex, selectedOptionIndex);
        currentQuestionIndex++;

        if (currentQuestionIndex >= testModel.getQuestionCount()) {
            resultModel.finalizeResult();
        }

        updateView();
    }

    // 선택지 버튼 이벤트 처리
    private class OptionButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton targetButton = (JButton) e.getSource();
            int selectedOptionIndex =
                    view.getOptionButtonIndex(targetButton);

            handleOptionSelected(selectedOptionIndex);
        }
    }
}
package view;

import controller.NavigationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 성향 테스트 화면
 */
public class TestView extends BaseView {

    private JLabel progressLabel;
    private JLabel questionLabel;
    private JButton[] optionButtons;
    private JButton backButton;

    public TestView(NavigationController navController) {
        super(navController);
    }

    @Override
    protected JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // 진행 상태 표시
        progressLabel = new JLabel("질문 1 / 7");
        progressLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        progressLabel.setBounds(50, 40, 200, 25);
        panel.add(progressLabel);

        // 질문 표시
        questionLabel = new JLabel(
                "공부할 때 가장 중요하게 생각하는 요소는 무엇인가요?"
        );
        questionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        questionLabel.setBounds(50, 90, 700, 40);
        panel.add(questionLabel);

        // 선택지 버튼
        optionButtons = new JButton[4];

        int startY = 160;

        for (int i = 0; i < optionButtons.length; i++) {

            optionButtons[i] = new JButton();

            optionButtons[i].setFont(
                    new Font("맑은 고딕", Font.PLAIN, 15)
            );

            optionButtons[i].setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            optionButtons[i].setBounds(
                    70,
                    startY + (i * 55),
                    660,
                    45
            );

            panel.add(optionButtons[i]);
        }

        // 이전 버튼
        backButton = new JButton("이전 화면으로");

        backButton.setFont(
                new Font("맑은 고딕", Font.PLAIN, 14)
        );

        backButton.setBounds(
                325,
                410,
                150,
                35
        );

        panel.add(backButton);

        return panel;
    }

    /**
     * 질문 및 선택지 표시
     */
    public void showQuestion(
            String query,
            String[] options
    ) {

        questionLabel.setText(query);

        for (int i = 0; i < optionButtons.length; i++) {

            if (i < options.length) {

                optionButtons[i].setText(
                        "  " + options[i]
                );

                optionButtons[i].setVisible(true);

            } else {

                optionButtons[i].setVisible(false);
            }
        }
    }

    /**
     * 진행 상태 갱신
     */
    public void updateProgress(
            int currentQuestion,
            int totalQuestion
    ) {

        progressLabel.setText(
                "질문 "
                        + currentQuestion
                        + " / "
                        + totalQuestion
        );
    }

    /**
     * 모든 테스트가 종료되었을 때 결과 화면으로 전환
     */
    public void navigateToRecommendResult() {
        navController.navigateTo("RECOMMEND_RESULT");
    }

    /**
     * 클릭된 버튼 인덱스 반환
     */
    public int getOptionButtonIndex(
            JButton button
    ) {

        for (int i = 0; i < optionButtons.length; i++) {

            if (optionButtons[i] == button) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 선택지 버튼 리스너 등록
     */
    public void addOptionButtonListener(
            ActionListener listener
    ) {

        for (JButton button : optionButtons) {
            button.addActionListener(listener);
        }
    }

    /**
     * 이전 버튼 리스너 등록
     */
    public void addBackButtonListener(
            ActionListener listener
    ) {
        backButton.addActionListener(listener);
    }
}
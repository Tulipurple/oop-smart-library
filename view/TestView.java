package view;

import controller.NavigationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TestView extends BaseView {

    private JLabel progressLabel;
    private JLabel questionLabel;
    private JButton[] optionButtons;
    private JButton backButton;

    private JPanel resultPanel;
    private JButton resultButton;

    public TestView(NavigationController navController) {
        super(navController);
    }

    @Override
    protected JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        progressLabel = new JLabel("질문 1 / 7");
        progressLabel.setFont(
                new Font("맑은 고딕", Font.BOLD, 16)
        );
        progressLabel.setBounds(
                50, 40, 200, 25
        );
        panel.add(progressLabel);

        questionLabel = new JLabel();
        questionLabel.setFont(
                new Font("맑은 고딕", Font.BOLD, 20)
        );
        questionLabel.setBounds(
                50, 90, 700, 40
        );
        panel.add(questionLabel);

        optionButtons = new JButton[4];

        int startY = 160;

        for (int i = 0; i < optionButtons.length; i++) {

            optionButtons[i] = new JButton();

            optionButtons[i].setFont(
                    new Font("맑은 고딕",
                            Font.PLAIN,
                            15)
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

        backButton =
                new JButton("이전 화면으로");

        backButton.setFont(
                new Font("맑은 고딕",
                        Font.PLAIN,
                        14)
        );

        backButton.setBounds(
                325,
                540,
                150,
                35
        );

        panel.add(backButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(null);
        resultPanel.setBackground(
                new Color(250,250,250)
        );

        resultPanel.setBounds(
                100,
                100,
                600,
                350
        );

        resultPanel.setVisible(false);

        JLabel checkLabel =
                new JLabel("완료");

        checkLabel.setFont(
                new Font("맑은 고딕",
                        Font.BOLD,
                        30)
        );

        checkLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        checkLabel.setBounds(
                200,
                30,
                200,
                50
        );

        resultPanel.add(checkLabel);

        JLabel mainLabel =
                new JLabel(
                        "성향 테스트가 완료되었습니다!"
                );

        mainLabel.setFont(
                TITLE_FONT
        );

        mainLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        mainLabel.setBounds(
                50,
                110,
                500,
                40
        );

        resultPanel.add(mainLabel);

        JLabel subLabel =
                new JLabel(
                        "추천 결과를 확인해보세요."
                );

        subLabel.setFont(
                BODY_FONT
        );

        subLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        subLabel.setBounds(
                50,
                160,
                500,
                30
        );

        resultPanel.add(subLabel);

        resultButton =
                new JButton(
                        "추천 결과 보기"
                );

        resultButton.setFont(
                BODY_FONT
        );

        resultButton.setBounds(
                200,
                220,
                200,
                40
        );

        resultPanel.add(resultButton);

        panel.add(resultPanel);

        return panel;
    }

    public void showQuestion(
            String query,
            String[] options
    ) {

        resultPanel.setVisible(false);

        progressLabel.setVisible(true);
        questionLabel.setVisible(true);
        backButton.setVisible(true);

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

    public void showCompletionView() {

        progressLabel.setVisible(false);
        questionLabel.setVisible(false);
        backButton.setVisible(false);

        for (JButton btn : optionButtons) {
            btn.setVisible(false);
        }

        resultPanel.setVisible(true);
    }

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

    public void addOptionButtonListener(
            ActionListener listener
    ) {

        for (JButton button : optionButtons) {
            button.addActionListener(listener);
        }
    }

    public void addBackButtonListener(
            ActionListener listener
    ) {
        backButton.addActionListener(listener);
    }

    public void addResultButtonListener(
            ActionListener listener
    ) {
        resultButton.addActionListener(listener);
    }

    public void navigateToRecommendResult() {

        navController.navigateTo(
                NavigationController.RECOMMEND
        );
    }

    public void navigateToHome() {

        navController.navigateTo(
                NavigationController.HOME
        );
    }
}
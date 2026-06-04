package view;

import controller.NavigationController;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Space;

/**
 * 추천 결과 완료 화면
 */
public class RecommendView extends BaseView {

    private JLabel userWelcomeLabel;
    private JPanel cardsContainerPanel;
    private JButton homeButton;

    public RecommendView(NavigationController navController) {
        super(navController);
    }

    @Override
    protected JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(VIEW_BG_COLOR);

        // 상단 안내 메시지
        userWelcomeLabel = new JLabel(
                "성향 분석을 바탕으로 추천된 최적의 공간입니다."
        );
        userWelcomeLabel.setFont(TITLE_FONT);
        userWelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userWelcomeLabel.setBounds(50, 40, 700, 40);
        panel.add(userWelcomeLabel);

        // 추천 공간 카드 컨테이너
        cardsContainerPanel = new JPanel();
        cardsContainerPanel.setLayout(null);
        cardsContainerPanel.setBackground(VIEW_BG_COLOR);
        cardsContainerPanel.setBounds(50, 110, 700, 260);
        panel.add(cardsContainerPanel);

        // 다시 추천받기 버튼
        homeButton = new JButton("다시 추천받기");
        homeButton.setFont(BODY_FONT);
        homeButton.setBounds(300, 400, 200, 40);
        panel.add(homeButton);

        return panel;
    }

    /**
     * 추천 좌석 결과 카드 동적 렌더링
     */
    public void displayRecommendations(
            String userName,
            List<Space> recommendedSpaces
    ) {

        userWelcomeLabel.setText(
                userName + "님의 성향 분석 기반 최적의 추천 좌석입니다."
        );

        cardsContainerPanel.removeAll();

        int cardWidth = 210;
        int cardHeight = 240;
        int gap = 25;

        for (int i = 0; i < recommendedSpaces.size(); i++) {

            Space space = recommendedSpaces.get(i);

            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBackground(Color.WHITE);
            card.setBorder(
                    new LineBorder(
                            new Color(180, 200, 220),
                            1
                    )
            );

            card.setBounds(
                    i * (cardWidth + gap),
                    10,
                    cardWidth,
                    cardHeight
            );

            // 순위 표시
            JLabel rankLabel =
                    new JLabel(String.valueOf(i + 1));

            rankLabel.setFont(
                    new Font("Arial", Font.BOLD, 14)
            );

            rankLabel.setBounds(12, 12, 25, 25);

            rankLabel.setBorder(
                    new LineBorder(
                            new Color(140, 160, 180),
                            1
                    )
            );

            rankLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            card.add(rankLabel);

            // 공간 이름
            JLabel nameLabel =
                    new JLabel(space.getName());

            nameLabel.setFont(
                    new Font("맑은 고딕", Font.BOLD, 16)
            );

            nameLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            nameLabel.setBounds(
                    10,
                    65,
                    cardWidth - 20,
                    30
            );

            card.add(nameLabel);

            // 공간 위치
            JLabel locLabel =
                    new JLabel(space.getLocation());

            locLabel.setFont(BODY_FONT);
            locLabel.setForeground(Color.GRAY);

            locLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            locLabel.setBounds(
                    10,
                    105,
                    cardWidth - 20,
                    25
            );

            card.add(locLabel);

            // 상세 보기 버튼
            JButton detailButton =
                    new JButton("상세 보기");

            detailButton.setFont(BODY_FONT);

            detailButton.setBounds(
                    50,
                    170,
                    110,
                    32
            );

            detailButton.addActionListener(
                    e -> navController.navigateTo(
                            NavigationController.SPACE_DETAIL,
                            space
                    )
            );

            card.add(detailButton);

            cardsContainerPanel.add(card);
        }

        cardsContainerPanel.revalidate();
        cardsContainerPanel.repaint();
    }

    /**
     * 다시 추천받기 버튼 리스너 등록
     */
    public void addRecommendButtonListener(
            ActionListener listener
    ) {
        homeButton.addActionListener(listener);
    }
}
package view;

import controller.NavigationController;
import controller.SpaceController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Review;
import model.Space;
import model.SpaceType;
import model.StudyRoom;

public class SpaceDetailView extends BaseView {

    private SpaceController controller;

    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel locationLabel;
    private JLabel capacityLabel;
    private JLabel minPeopleLabel;

    private JLabel noiseLabel;
    private JLabel crowdLabel;
    private JLabel cleanLabel;

    private JTextArea reviewArea;

    public SpaceDetailView(NavigationController navController) {
        super(navController);
    }

    public void setController(SpaceController controller) {
        this.controller = controller;
    }

    @Override
    public void onShow(Object data) {
        if (data instanceof Space && controller != null) {
            Space space = (Space) data;
            controller.loadSpaceDetails(space);
        }
    }

    @Override
    protected JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(VIEW_BG_COLOR);

        JLabel titleLabel =
                new JLabel("공간 상세 정보");

        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        titleLabel.setBounds(
                50,
                35,
                700,
                40
        );

        panel.add(titleLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(
                new LineBorder(Color.LIGHT_GRAY)
        );

        infoPanel.setBounds(
                70,
                100,
                660,
                150
        );

        panel.add(infoPanel);

        JLabel typeTitle = new JLabel("유형");
        typeTitle.setFont(BODY_FONT);
        typeTitle.setBounds(30,20,90,25);
        infoPanel.add(typeTitle);

        typeLabel = new JLabel("-");
        typeLabel.setFont(BODY_FONT);
        typeLabel.setBounds(130,20,450,25);
        infoPanel.add(typeLabel);

        JLabel nameTitle = new JLabel("공간명");
        nameTitle.setFont(BODY_FONT);
        nameTitle.setBounds(30,50,90,25);
        infoPanel.add(nameTitle);

        nameLabel = new JLabel("-");
        nameLabel.setFont(BODY_FONT);
        nameLabel.setBounds(130,50,450,25);
        infoPanel.add(nameLabel);

        JLabel locationTitle = new JLabel("위치");
        locationTitle.setFont(BODY_FONT);
        locationTitle.setBounds(30,80,90,25);
        infoPanel.add(locationTitle);

        locationLabel = new JLabel("-");
        locationLabel.setFont(BODY_FONT);
        locationLabel.setBounds(130,80,450,25);
        infoPanel.add(locationLabel);

        JLabel capacityTitle =
                new JLabel("수용 인원");

        capacityTitle.setFont(BODY_FONT);
        capacityTitle.setBounds(30,110,90,25);
        infoPanel.add(capacityTitle);

        capacityLabel = new JLabel("-");
        capacityLabel.setFont(BODY_FONT);
        capacityLabel.setBounds(130,110,120,25);
        infoPanel.add(capacityLabel);

        JLabel minPeopleTitle =
                new JLabel("최소 인원");

        minPeopleTitle.setFont(BODY_FONT);
        minPeopleTitle.setBounds(300,110,90,25);
        infoPanel.add(minPeopleTitle);

        minPeopleLabel = new JLabel("-");
        minPeopleLabel.setFont(BODY_FONT);
        minPeopleLabel.setBounds(400,110,120,25);
        infoPanel.add(minPeopleLabel);

        JLabel statusTitle =
                new JLabel("최근 상태 평균");

        statusTitle.setFont(
                new Font("맑은 고딕",
                        Font.BOLD,
                        18)
        );

        statusTitle.setBounds(
                70,
                280,
                250,
                30
        );

        panel.add(statusTitle);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(
                new LineBorder(Color.LIGHT_GRAY)
        );

        statusPanel.setBounds(
                70,
                320,
                260,
                140
        );

        panel.add(statusPanel);

        JLabel noiseTitle =
                new JLabel("소음");

        noiseTitle.setFont(BODY_FONT);
        noiseTitle.setBounds(30,25,80,25);
        statusPanel.add(noiseTitle);

        noiseLabel = new JLabel("-");
        noiseLabel.setFont(BODY_FONT);
        noiseLabel.setBounds(120,25,100,25);
        statusPanel.add(noiseLabel);

        JLabel crowdTitle =
                new JLabel("혼잡도");

        crowdTitle.setFont(BODY_FONT);
        crowdTitle.setBounds(30,60,80,25);
        statusPanel.add(crowdTitle);

        crowdLabel = new JLabel("-");
        crowdLabel.setFont(BODY_FONT);
        crowdLabel.setBounds(120,60,100,25);
        statusPanel.add(crowdLabel);

        JLabel cleanTitle =
                new JLabel("청결도");

        cleanTitle.setFont(BODY_FONT);
        cleanTitle.setBounds(30,95,80,25);
        statusPanel.add(cleanTitle);

        cleanLabel = new JLabel("-");
        cleanLabel.setFont(BODY_FONT);
        cleanLabel.setBounds(120,95,100,25);
        statusPanel.add(cleanLabel);

        JLabel reviewTitle =
                new JLabel("후기 목록");

        reviewTitle.setFont(
                new Font("맑은 고딕",
                        Font.BOLD,
                        18)
        );

        reviewTitle.setBounds(
                380,
                280,
                250,
                30
        );

        panel.add(reviewTitle);

        reviewArea = new JTextArea();
        reviewArea.setFont(BODY_FONT);
        reviewArea.setEditable(false);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);

        JScrollPane scrollPane =
                new JScrollPane(reviewArea);

        scrollPane.setBounds(
                380,
                320,
                350,
                140
        );

        panel.add(scrollPane);

        JButton backButton =
                new JButton("이전 화면으로");

        backButton.setFont(BODY_FONT);

        backButton.setBounds(
                220,
                500,
                150,
                40
        );

        backButton.addActionListener(
                e -> navController.navigateBack()
        );

        panel.add(backButton);

        JButton homeButton =
                new JButton("홈으로");

        homeButton.setFont(BODY_FONT);

        homeButton.setBounds(
                430,
                500,
                150,
                40
        );

        homeButton.addActionListener(
                e -> navController.navigateTo(
                        NavigationController.HOME
                )
        );

        panel.add(homeButton);

        return panel;
    }

    public void updateSpaceDetails(
            Space space,
            double avgNoise,
            double avgCrowd,
            double avgClean,
            List<Review> reviews
    ) {

        nameLabel.setText(space.getName());
        locationLabel.setText(space.getLocation());

        if (space.getSpaceType()
                == SpaceType.SEAT) {

            typeLabel.setText("좌석");

        } else {

            typeLabel.setText("스터디룸");
        }

        if (space instanceof StudyRoom) {

            StudyRoom room =
                    (StudyRoom) space;

            capacityLabel.setText(
                    room.getCapacity() + "명"
            );

            minPeopleLabel.setText(
                    room.getMinPeople() + "명"
            );

        } else {

            capacityLabel.setText("1명");
            minPeopleLabel.setText("-");
        }

        noiseLabel.setText(
                String.format("%.1f", avgNoise)
        );

        crowdLabel.setText(
                String.format("%.1f", avgCrowd)
        );

        cleanLabel.setText(
                String.format("%.1f", avgClean)
        );

        reviewArea.setText("");

        if (reviews == null
                || reviews.isEmpty()) {

            reviewArea.setText(
                    "등록된 후기가 없습니다."
            );

            return;
        }

        for (Review review : reviews) {

            reviewArea.append(
                    makeStars(review.getRating())
            );

            reviewArea.append("\n");
            reviewArea.append(
                    review.getComment()
            );

            reviewArea.append("\n");
            reviewArea.append(
                    review.getCreatedAt()
                            .toString()
            );

            reviewArea.append("\n\n");
        }
    }

    private String makeStars(int rating) {

        String stars = "";

        for (int i = 0; i < rating; i++) {
            stars += "*";
        }

        for (int i = rating; i < 5; i++) {
            stars += "-";
        }

        return stars;
    }

    public void showMessage(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }
}
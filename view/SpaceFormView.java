package view;

import controller.NavigationController;
import controller.SpaceFormController;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 공간 이용 후 후기(별점/한줄평)와 최신 상태(소음/혼잡도/청결도) 등록 화면
 */
public class SpaceFormView extends BaseView {

    private final SpaceFormController controller;

    private JComboBox<String> spaceComboBox; // 공간 선택 콤보박스

    private JCheckBox reviewCheckBox; //후기 등록 여부
    private JCheckBox statusCheckBox; //상태 등록 여부

    private JPanel reviewPanel; // 후기 입력 패널
    private JPanel statusPanel; // 상태 입력 패널

    private JLabel[] starLabels; //별점 라벨
    private int currentRating; //현재 선택된 별점
    private JTextField commentField; //한 줄 평

    private JSlider noiseSlider; //소음도 (1~5)
    private JSlider crowdSlider; //혼잡도 (1~5)
    private JSlider cleanSlider; //청결도 (1~5)

    private JLabel feedbackLabel; //결과 메시지

    // 생성자
    public SpaceFormView(NavigationController navController,
                         SpaceFormController controller) {
        super(navController);
        this.controller = controller;
    }

    // ───────────────────────────────────────────────────────
    // BaseView 추상 메서드 구현
    // ───────────────────────────────────────────────────────
    @Override
    protected JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(VIEW_BG_COLOR);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        content.add(buildTopPanel(),    BorderLayout.NORTH);
        content.add(buildMiddlePanel(), BorderLayout.CENTER);
        content.add(buildBottomPanel(), BorderLayout.SOUTH);

        return content;
    }

    // 화면 진입 시 공간 목록 갱신
    @Override
    public void onShow() {
        refreshSpaceList();
        resetForm();
    }

    // ───────────────────────────────────────────────────────
    // UI 컴포넌트 빌드
    // ───────────────────────────────────────────────────────

    // 상단: 공간 선택 드롭다운
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(VIEW_BG_COLOR);

        spaceComboBox = new JComboBox<>();
        spaceComboBox.setFont(BODY_FONT);
        spaceComboBox.setPreferredSize(new Dimension(300, 30));

        // 공간 선택 시 Controller에 spaceId 전달
        spaceComboBox.addActionListener(e -> {
            int index = spaceComboBox.getSelectedIndex();
            if (index >= 0) {
                controller.setCurrentSpaceId(controller.getSpaceIdByIndex(index));
            }
        });

        JLabel spaceLabel = makeLabel("1. 공간 선택   ");
        spaceLabel.setFont(BODY_FONT.deriveFont(Font.BOLD));
        panel.add(spaceLabel);
        panel.add(spaceComboBox);
        return panel;
    }

    // 중앙: 체크박스 / 후기 입력 / 상태 입력
    private JPanel buildMiddlePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(VIEW_BG_COLOR);

        panel.add(buildCheckBoxPanel(), BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rightPanel.setBackground(VIEW_BG_COLOR);
        rightPanel.add(buildReviewInputPanel());
        rightPanel.add(buildStatusInputPanel());

        panel.add(rightPanel, BorderLayout.CENTER);
        return panel;
    }

    // 후기/상태 등록 여부 체크박스 패널
    private JPanel buildCheckBoxPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 4));
        panel.setPreferredSize(new Dimension(160, 0));
        panel.setBackground(VIEW_BG_COLOR);
        panel.setBorder(new TitledBorder("2. 등록할 항목 선택"));

        reviewCheckBox = new JCheckBox("후기 등록");
        statusCheckBox = new JCheckBox("상태 등록");
        reviewCheckBox.setBackground(VIEW_BG_COLOR);
        statusCheckBox.setBackground(VIEW_BG_COLOR);
        reviewCheckBox.setFont(BODY_FONT);
        statusCheckBox.setFont(BODY_FONT);

        // 체크박스 선택에 따라 입력 패널 동적 표시/숨김
        reviewCheckBox.addActionListener(e -> {
            if (reviewPanel != null) {
                reviewPanel.setVisible(reviewCheckBox.isSelected());
            }
        });

        statusCheckBox.addActionListener(e -> {
            if (statusPanel != null) {
                statusPanel.setVisible(statusCheckBox.isSelected());
            }
        });

        panel.add(reviewCheckBox);
        panel.add(statusCheckBox);
        return panel;
    }

    // 후기(별점·한 줄 평) 입력 패널
    private JPanel buildReviewInputPanel() {
        reviewPanel = new JPanel(new GridLayout(5, 1, 0, 0));
        reviewPanel.setBackground(VIEW_BG_COLOR);
        reviewPanel.setBorder(new TitledBorder("후기 입력"));
        reviewPanel.setVisible(false);

        reviewPanel.add(makeLabel("별점:"));
        reviewPanel.add(buildStarPanel());
        reviewPanel.add(makeLabel("한 줄 평:"));
        commentField = new JTextField();
        reviewPanel.add(commentField);
        reviewPanel.add(new JLabel()); // 공백
        return reviewPanel;
    }

    // 별점 패널
    private JPanel buildStarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panel.setBackground(VIEW_BG_COLOR);

        starLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            final int starIndex = i + 1;
            starLabels[i] = new JLabel("☆");
            starLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 24));
            starLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // 별점 선택 이벤트
            starLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentRating = starIndex;
                    updateStarDisplay();
                }
            });
            panel.add(starLabels[i]);
        }
        updateStarDisplay();
        return panel;
    }

    // 상태(소음도·혼잡도·청결도) 입력 패널
    private JPanel buildStatusInputPanel() {
        statusPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        statusPanel.setBackground(VIEW_BG_COLOR);
        statusPanel.setBorder(new TitledBorder("상태 입력"));
        statusPanel.setVisible(false); // 기본 숨김

        noiseSlider = makeSlider();
        crowdSlider = makeSlider();
        cleanSlider = makeSlider();

        statusPanel.add(buildSliderRow("소음도:", noiseSlider));
        statusPanel.add(buildSliderRow("혼잡도:", crowdSlider));
        statusPanel.add(buildSliderRow("청결도:", cleanSlider));
        return statusPanel;
    }

    // 슬라이더 한 행 생성 헬퍼
    private JPanel buildSliderRow(String labelText, JSlider slider) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(VIEW_BG_COLOR);
        row.add(makeLabel(labelText), BorderLayout.WEST);
        row.add(slider, BorderLayout.CENTER);
        return row;
    }

    // 하단: 등록 버튼 + 피드백 메시지 패널
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(VIEW_BG_COLOR);

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        submitPanel.setBackground(VIEW_BG_COLOR);

        JButton submitButton = new JButton("등록하기");
        submitButton.setFont(BODY_FONT);
        submitButton.setPreferredSize(new Dimension(200, 35));

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(BODY_FONT);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 등록 버튼 클릭 이벤트
        submitButton.addActionListener(e -> handleSubmit());

        submitPanel.add(submitButton);

        JLabel note = new JLabel("※ 후기와 상태는 선택적으로 등록할 수 있으며, 입력한 항목만 익명으로 저장됩니다.");
        note.setForeground(Color.GRAY);
        note.setFont(new Font("맑은 고딕", Font.PLAIN, 11));

        panel.add(submitPanel, BorderLayout.NORTH);
        panel.add(feedbackLabel, BorderLayout.CENTER);
        panel.add(note, BorderLayout.SOUTH);
        return panel;
    }


    // 1-5 범위 슬라이더 생성 헬퍼
    private JSlider makeSlider() {
        JSlider slider = new JSlider(1, 5, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFont(new Font("SansSerif", Font.PLAIN, 10));
        slider.setBackground(VIEW_BG_COLOR);
        return slider;
    }

    // 공통 라벨 생성 헬퍼
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        return label;
    }

    // ───────────────────────────────────────────────────────
    // 이벤트 처리
    // ───────────────────────────────────────────────────────

    private void handleSubmit() {
        boolean wantsReview = reviewCheckBox.isSelected();
        boolean wantsStatus = statusCheckBox.isSelected();

        // 아무것도 선택하지 않은 경우 안내 후 넘어감
        if (!wantsReview && !wantsStatus) {
            showFeedback("입력할 항목을 먼저 선택해 주세요.", Color.RED);
            return;
        }

        // 후기 선택 시 별점 또는 한 줄 평 미입력 안내
        if (wantsReview && currentRating < 1) {
            showFeedback("별점을 선택해 주세요.", Color.RED);
            return;
        }
        if (wantsReview && commentField.getText().isBlank()) {
            showFeedback("한 줄 평을 입력해 주세요.", Color.RED);
            return;
        }

        controller.handleSubmit(
                wantsReview,
                wantsStatus,
                currentRating,
                commentField.getText().trim(),
                noiseSlider.getValue(),
                crowdSlider.getValue(),
                cleanSlider.getValue()
        );
    }

    // ───────────────────────────────────────────────────────
    // Controller가 호출하는 공개 메서드
    // ───────────────────────────────────────────────────────

    // 결과 메세지
    public void showFeedback(String message, Color color) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(color);
    }

    // 폼 초기화
    public void resetForm() {
        if (reviewCheckBox != null) reviewCheckBox.setSelected(false);
        if (statusCheckBox != null) statusCheckBox.setSelected(false);
        if (reviewPanel   != null) reviewPanel.setVisible(false);
        if (statusPanel   != null) statusPanel.setVisible(false);
        if (commentField   != null) commentField.setText("");
        if (noiseSlider    != null) noiseSlider.setValue(1);
        if (crowdSlider    != null) crowdSlider.setValue(1);
        if (cleanSlider    != null) cleanSlider.setValue(1);
        if (feedbackLabel  != null) feedbackLabel.setText(" ");
        currentRating = 0;
        updateStarDisplay();
    }

    // ───────────────────────────────────────────────────────
    // 내부 유틸리티
    // ───────────────────────────────────────────────────────

    // 별점 모양 업데이트
    private void updateStarDisplay() {
        if (starLabels == null) return;
        for (int i = 0; i < starLabels.length; i++) {
            starLabels[i].setText(i < currentRating ? "★" : "☆");
        }
    }

    // 공간 목록 갱신
    private void refreshSpaceList() {
        if (spaceComboBox == null) return;
        spaceComboBox.removeAllItems();
        List<String> names = controller.getSpaceNames();
        for (String name : names) {
            spaceComboBox.addItem(name);
        }
        if (spaceComboBox.getItemCount() > 0) {
            spaceComboBox.setSelectedIndex(0);
        } else {
            controller.setCurrentSpaceId(null);  // 목록 비어있으면 초기화
        }
    }
}
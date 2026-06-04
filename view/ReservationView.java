package view;

import controller.ReservationController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 신규 예약 신청 화면
 */
public class ReservationView extends JFrame {

    private final ReservationController reservationController;
    private final User currentUser;

    private JTextField spaceIdField;
    private JTextField startTimeField;
    private JTextField endTimeField;

    private JButton submitButton;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReservationView(
            ReservationController reservationController,
            User currentUser
    ) {
        this.reservationController = reservationController;
        this.currentUser = currentUser;

        setTitle("신규 예약 신청");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    /**
     * UI 구성
     */
    private void initUI() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 제목
        JLabel titleLabel =
                new JLabel("도서관 공간 예약", JLabel.CENTER);

        titleLabel.setFont(
                new Font("맑은 고딕", Font.BOLD, 18)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // 공간 ID
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("공간 ID"), gbc);

        spaceIdField = new JTextField(15);

        gbc.gridx = 1;
        panel.add(spaceIdField, gbc);

        // 시작 시간
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("시작 시간"), gbc);

        startTimeField =
                new JTextField(
                        LocalDateTime.now()
                                .format(formatter)
                );

        gbc.gridx = 1;
        panel.add(startTimeField, gbc);

        // 종료 시간
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("종료 시간"), gbc);

        endTimeField =
                new JTextField(
                        LocalDateTime.now()
                                .plusHours(2)
                                .format(formatter)
                );

        gbc.gridx = 1;
        panel.add(endTimeField, gbc);

        // 형식 안내
        JLabel infoLabel =
                new JLabel(
                        "(yyyy-MM-dd HH:mm 형식)",
                        JLabel.CENTER
                );

        infoLabel.setForeground(Color.GRAY);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(infoLabel, gbc);

        // 예약 버튼
        submitButton = new JButton("예약 신청");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        panel.add(submitButton, gbc);

        add(panel);

        submitButton.addActionListener(
                e -> handleReservation()
        );
    }

    /**
     * 예약 처리
     */
    private void handleReservation() {

        String spaceId =
                spaceIdField.getText().trim();

        String startText =
                startTimeField.getText().trim();

        String endText =
                endTimeField.getText().trim();

        if (spaceId.isEmpty()
                || startText.isEmpty()
                || endText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "모든 항목을 입력해주세요."
            );

            return;
        }

        try {

            LocalDateTime startTime =
                    LocalDateTime.parse(
                            startText,
                            formatter
                    );

            LocalDateTime endTime =
                    LocalDateTime.parse(
                            endText,
                            formatter
                    );

            if (!startTime.isBefore(endTime)) {

                JOptionPane.showMessageDialog(
                        this,
                        "종료 시간은 시작 시간보다 늦어야 합니다."
                );

                return;
            }

            boolean success =
                    reservationController.makeReservation(
                            currentUser.getStudentId(),
                            spaceId,
                            startTime,
                            endTime
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "예약이 완료되었습니다."
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "이미 예약된 시간입니다."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "날짜 형식이 올바르지 않습니다.\n예) 2026-06-04 14:00"
            );
        }
    }
}

package view;

import controller.MyReservationController;
import model.Reservation;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 내 예약 현황 조회 화면
 */
public class MyReservationView extends JFrame {

    private final MyReservationController myReservationController;
    private final User currentUser;

    private JTable activeTable;
    private JTable pastTable;

    private DefaultTableModel activeModel;
    private DefaultTableModel pastModel;

    private JButton refreshButton;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm"
            );

    public MyReservationView(
            MyReservationController myReservationController,
            User currentUser
    ) {

        this.myReservationController =
                myReservationController;

        this.currentUser =
                currentUser;

        setTitle("내 예약 현황");

        setSize(700, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        initUI();

        loadReservationData();
    }

    /**
     * UI 구성
     */
    private void initUI() {

        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel =
                new JPanel(new BorderLayout());

        JLabel userLabel =
                new JLabel(
                        currentUser.getName()
                                + " ("
                                + currentUser.getStudentId()
                                + ")님의 예약 현황"
                );

        userLabel.setFont(
                new Font(
                        "맑은 고딕",
                        Font.BOLD,
                        15
                )
        );

        refreshButton =
                new JButton("새로고침");

        topPanel.add(
                userLabel,
                BorderLayout.WEST
        );

        topPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // 중앙 패널
        JPanel centerPanel =
                new JPanel();

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        String[] columns = {
                "예약 ID",
                "공간 ID",
                "시작 시간",
                "종료 시간",
                "상태"
        };

        // 현재 예약
        JLabel activeLabel =
                new JLabel(
                        "현재 예약"
                );

        activeModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        activeTable =
                new JTable(activeModel);

        JScrollPane activeScroll =
                new JScrollPane(activeTable);

        // 지난 예약
        JLabel pastLabel =
                new JLabel(
                        "지난 예약"
                );

        pastModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        pastTable =
                new JTable(pastModel);

        JScrollPane pastScroll =
                new JScrollPane(pastTable);

        centerPanel.add(activeLabel);
        centerPanel.add(activeScroll);

        centerPanel.add(Box.createVerticalStrut(20));

        centerPanel.add(pastLabel);
        centerPanel.add(pastScroll);

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        refreshButton.addActionListener(
                e -> loadReservationData()
        );
    }

    /**
     * 예약 데이터 로드
     */
    private void loadReservationData() {

        activeModel.setRowCount(0);

        pastModel.setRowCount(0);

        String studentId =
                currentUser.getStudentId();

        List<Reservation> activeReservations =
                myReservationController
                        .getActiveReservations(
                                studentId
                        );

        for (Reservation reservation
                : activeReservations) {

            activeModel.addRow(
                    new Object[]{
                            reservation.getReservationId(),
                            reservation.getSpaceId(),
                            reservation.getStartTime()
                                    .format(formatter),
                            reservation.getEndTime()
                                    .format(formatter),
                            reservation.getStatus()
                    }
            );
        }

        List<Reservation> pastReservations =
                myReservationController
                        .getPastReservations(
                                studentId
                        );

        for (Reservation reservation
                : pastReservations) {

            pastModel.addRow(
                    new Object[]{
                            reservation.getReservationId(),
                            reservation.getSpaceId(),
                            reservation.getStartTime()
                                    .format(formatter),
                            reservation.getEndTime()
                                    .format(formatter),
                            reservation.getStatus()
                    }
            );
        }
    }
}
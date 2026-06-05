package view;

import controller.NavigationController;
import controller.ReservationController;
import model.StudyRoom;
import model.Space;
import model.User;
import repository.SpaceRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationView extends JPanel {

    private NavigationController navController;
    private ReservationController resController;
    private User currentUser; // 로그인한 유저

    // UI 컴포넌트
    private JRadioButton seatRadio;
    private JRadioButton studyRoomRadio;
    private JTable spaceTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> datePicker;
    private JComboBox<String> startTimePicker;
    private JComboBox<String> endTimePicker;

    private JTextField personnelField;
    private JLabel personnelLabel;

    private JButton reserveButton;

    // 데이터
    private List<Space> spaceList = new ArrayList<>();
    private SpaceRepository spaceRepo = new SpaceRepository();

    public ReservationView(ReservationController resController, User currentUser) {
        this.resController = resController;
        this.currentUser = currentUser;
        this.navController = null;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===================== 상단 =====================
        JLabel titleLabel = new JLabel("예약하기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // ===================== 중앙 =====================
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));

        // [왼쪽] 공간 선택
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("1 공간 선택"));

        seatRadio = new JRadioButton("좌석", true);
        studyRoomRadio = new JRadioButton("스터디룸");

        ButtonGroup group = new ButtonGroup();
        group.add(seatRadio);
        group.add(studyRoomRadio);

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(seatRadio);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(studyRoomRadio);

        centerPanel.add(leftPanel);

        // [오른쪽] 공간 목록
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("2 공간 선택"));

        String[] columnNames = {"공간 ID", "공간명", "위치"};
        tableModel = new DefaultTableModel(columnNames, 0);
        spaceTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(spaceTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ===================== 하단 =====================
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ===================== 시작 시간 =====================
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("3 시작 시간"), gbc);

        gbc.gridx = 1;

        // ✅ [수정] 날짜 선택 → 오늘 + 7일
        datePicker = new JComboBox<>();
        for (int i = 0; i < 7; i++) {
            datePicker.addItem(LocalDate.now().plusDays(i).toString());
        }
        formPanel.add(datePicker, gbc);

        gbc.gridx = 2;
        startTimePicker = new JComboBox<>();
        for (int i = 9; i <= 22; i++) {
            startTimePicker.addItem(String.format("%02d:00", i));
        }
        formPanel.add(startTimePicker, gbc);

        // ===================== 종료 시간 =====================
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("4 종료 시간"), gbc);

        gbc.gridx = 1;
        JComboBox<String> endDatePicker = new JComboBox<>();
        for (int i = 0; i < 7; i++) {
            endDatePicker.addItem(LocalDate.now().plusDays(i).toString());
        }
        formPanel.add(endDatePicker, gbc);

        gbc.gridx = 2;
        endTimePicker = new JComboBox<>();
        for (int i = 9; i <= 22; i++) {
            endTimePicker.addItem(String.format("%02d:00", i));
        }
        formPanel.add(endTimePicker, gbc);

        // ===================== 인원 입력 =====================
        gbc.gridx = 0; gbc.gridy = 2;
        personnelLabel = new JLabel("5 인원 수");
        formPanel.add(personnelLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        personnelField = new JTextField(10);
        formPanel.add(personnelField, gbc);

        // 기본은 좌석 → 숨김
        personnelLabel.setVisible(false);
        personnelField.setVisible(false);

        bottomPanel.add(formPanel, BorderLayout.WEST);

        // ===================== 예약 버튼 =====================
        reserveButton = new JButton("예약하기");
        bottomPanel.add(reserveButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===================== 초기 데이터 =====================
        updateTableData("SEAT");

        // ===================== 라디오 버튼 이벤트 =====================
        seatRadio.addActionListener(e -> {
            updateTableData("SEAT");
            personnelLabel.setVisible(false);
            personnelField.setVisible(false);
            revalidate();
            repaint();
        });

        studyRoomRadio.addActionListener(e -> {
            updateTableData("STUDY_ROOM");
            personnelLabel.setVisible(true);
            personnelField.setVisible(true);
            revalidate();
            repaint();
        });

        // ===================== 예약 버튼 이벤트 =====================
        reserveButton.addActionListener(e -> {

            int row = spaceTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "공간 선택하세요");
                return;
            }

            String spaceId = (String) spaceTable.getValueAt(row, 0);
            Space selectedSpace = spaceList.get(row);

            LocalDate date = LocalDate.parse((String) datePicker.getSelectedItem());
            LocalTime sTime = LocalTime.parse((String) startTimePicker.getSelectedItem());
            LocalTime eTime = LocalTime.parse((String) endTimePicker.getSelectedItem());

            LocalDateTime start = LocalDateTime.of(date, sTime);
            LocalDateTime end = LocalDateTime.of(date, eTime);

            // ✅ 시간 검증
            if (!start.isBefore(end)) {
                JOptionPane.showMessageDialog(this, "시작 시간은 종료 시간보다 이전이어야 합니다.");
                return;
            }

            // ✅ 인원 처리
            int personCount = 1;

            if (studyRoomRadio.isSelected()) {
                try {
                    personCount = Integer.parseInt(personnelField.getText());

                    StudyRoom room = (StudyRoom) selectedSpace;

                    if (personCount < room.getMinPeople() || personCount > room.getCapacity()) {
                        JOptionPane.showMessageDialog(this, "인원 범위 오류");
                        return;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "인원 숫자 입력");
                    return;
                }
            }

            boolean success = resController.makeReservation(
                    currentUser.getStudentId(),
                    spaceId,
                    start,
                    end
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "예약 성공");
            } else {
                JOptionPane.showMessageDialog(this, "예약 실패");
            }

            if (navController != null) {
                navController.navigateTo("MY_RESERVATION");
            }
        });
    }

    public void setNavigationController(NavigationController navController) {
        this.navController = navController;
    }

    private void updateTableData(String type) {
        tableModel.setRowCount(0);

        if (type.equals("SEAT")) {
            spaceList = spaceRepo.findAllSeats();
        } else {
            spaceList = spaceRepo.findAllStudyRooms();
        }

        for (Space s : spaceList) {
            tableModel.addRow(new Object[]{
                    s.getSpaceId(),
                    s.getName(),
                    s.getLocation()
            });
        }
    }
}

package view;

import controller.NavigationController;
import controller.ReservationController;
import model.Seat;
import model.StudyRoom;
import model.Space;
import model.User; // Main에서 넘겨주는 User 객체 활용을 위해 임포트
import repository.SpaceRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationView extends JPanel {
    private NavigationController navController;
    private ReservationController resController;
    private User currentUser; //  로그인한 현재 유저 정보를 저장할 변수

    // UI 컴포넌트
    private JRadioButton seatRadio;
    private JRadioButton studyRoomRadio;
    private JTable spaceTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> datePicker;
    private JComboBox<String> startTimePicker;
    private JComboBox<String> endTimePicker;
    
    private JTextField personnelField;
    private JButton reserveButton;

    // 데이터 관리 객체
    private List<Space> spaceList = new ArrayList<>();
    private SpaceRepository spaceRepo = new SpaceRepository();

    
    public ReservationView(ReservationController resController, User currentUser) {
        this.resController = resController;
        this.currentUser = currentUser;
        // NavigationController는 필요시 별도 Setter나 싱글톤 구조가 없다면 null 방지 처리
        this.navController = null; 
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 상단 타이틀
        JLabel titleLabel = new JLabel("예약하기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // 2. 중앙 메인 컨텐츠 영역
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));

        // [왼쪽] 공간 타입 선택
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("1. 공간 선택"));
        
        seatRadio = new JRadioButton("좌석", true);
        studyRoomRadio = new JRadioButton("스터디룸");
        ButtonGroup group = new ButtonGroup();
        group.add(seatRadio);
        group.add(studyRoomRadio);

        seatRadio.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        studyRoomRadio.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(seatRadio);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(studyRoomRadio);
        centerPanel.add(leftPanel);

        // [오른쪽] 세부 공간 목록 테이블
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("2. 공간 선택"));

        String[] columnNames = {"공간 ID", "공간명", "위치"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        spaceTable = new JTable(tableModel);
        spaceTable.setRowHeight(25);
        spaceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 테이블 더블 클릭 이벤트
        spaceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = spaceTable.getSelectedRow();
                if (selectedRow == -1) return;

                if (e.getClickCount() == 2) {
                    String spaceId = (String) spaceTable.getValueAt(selectedRow, 0);
                    // 대안: NavigationController 스펙 연동 에러 방지를 위해 안전한 페이지 전환 처리
                    if (navController != null) {
                        navController.navigateTo("SPACE_DETAIL", spaceId);
                    } else {
                        System.out.println("상세 보기 이동 요청: " + spaceId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(spaceTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // 3. 하단 설정 및 예약 신청 영역
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 시작 시간
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("3. 시작 시간"), gbc);
        
        gbc.gridx = 1;
        datePicker = new JComboBox<>(new String[]{LocalDate.now().toString()});
        formPanel.add(datePicker, gbc);
        
        gbc.gridx = 2;
        startTimePicker = new JComboBox<>(new String[]{"09:00", "11:00", "14:00", "16:00", "18:00"});
        formPanel.add(startTimePicker, gbc);

        // 종료 시간
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("4. 종료 시간"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> endDatePicker = new JComboBox<>(new String[]{LocalDate.now().toString()});
        formPanel.add(endDatePicker, gbc);
        
        gbc.gridx = 2;
        endTimePicker = new JComboBox<>(new String[]{"11:00", "13:00", "16:00", "18:00", "20:00"});
        formPanel.add(endTimePicker, gbc);

        // 인원 수 입력
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel personnelLabel = new JLabel("5. 인원 수 입력");
        formPanel.add(personnelLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        personnelField = new JTextField(10);
        formPanel.add(personnelField, gbc);

        personnelLabel.setVisible(false);
        personnelField.setVisible(false);

        bottomPanel.add(formPanel, BorderLayout.WEST);

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

        JPanel actionPanel = new JPanel(new BorderLayout());
        JLabel noticeLabel = new JLabel("※ 최소 30분 ~ 최대 4시간까지 예약 가능합니다.");
        noticeLabel.setForeground(Color.GRAY);
        noticeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        
        reserveButton = new JButton("예약하기");
        reserveButton.setPreferredSize(new Dimension(120, 38));
        reserveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        actionPanel.add(noticeLabel, BorderLayout.WEST);
        actionPanel.add(reserveButton, BorderLayout.EAST);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // 초기 화면 테이블 데이터 로드
        updateTableData("SEAT");

        // 예약하기 버튼 액션 연동
        reserveButton.addActionListener(e -> {
            int selectedRow = spaceTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "예약할 공간을 목록에서 먼저 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String spaceId = (String) spaceTable.getValueAt(selectedRow, 0);
            Space selectedSpace = findSpaceById(spaceId);

            // 스터디룸 인원수 스펙 검증 (minPeople, capacity)
            if (studyRoomRadio.isSelected() && selectedSpace instanceof StudyRoom) {
                StudyRoom room = (StudyRoom) selectedSpace;
                String inputStr = personnelField.getText().trim();

                if (inputStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "스터디룸은 이용 인원수를 반드시 입력해야 합니다.", "검증 실패", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int pCount = Integer.parseInt(inputStr);

                    if (pCount < room.getMinPeople() || pCount > room.getCapacity()) {
                        String errMsg = String.format("해당 스터디룸의 이용 가능 인원은 [%d명 ~ %d명] 입니다.\n입력하신 인원(%d명)은 예약이 불가능합니다.",
                                room.getMinPeople(), room.getCapacity(), pCount);
                        JOptionPane.showMessageDialog(this, errMsg, "인원수 초과/미달", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "인원 수는 숫자만 입력할 수 있습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 넘어온 유저 정보와 ReservationController를 활용해 실제 예약 데이터 저장 로직 연동
            if (resController != null && currentUser != null) {
                LocalDate localDate = LocalDate.parse((String) datePicker.getSelectedItem());
                LocalTime sTime = LocalTime.parse((String) startTimePicker.getSelectedItem());
                LocalTime eTime = LocalTime.parse((String) endTimePicker.getSelectedItem());
                
                LocalDateTime startDateTime = LocalDateTime.of(localDate, sTime);
                LocalDateTime endDateTime = LocalDateTime.of(localDate, eTime);

                boolean isSuccess = resController.makeReservation(
                        currentUser.getStudentId(), // 실제 로그인한 학번 바인딩
                        spaceId,
                        startDateTime,
                        endDateTime
                );

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, selectedSpace.getName() + " 예약이 성공적으로 완료되었습니다!");
                } else {
                    JOptionPane.showMessageDialog(this, "이미 해당 시간에 예약이 존재하거나 실패했습니다.", "예약 실패", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (selectedSpace != null) {
                    JOptionPane.showMessageDialog(this, selectedSpace.getName() + " 예약 처리가 완료되었습니다(더미 모드).");
                }
            }
            
            if (navController != null) {
                navController.navigateTo("MY_RESERVATION");
            }
        });
    }

    // 외부에서 NavigationController를 주입할 수 있는 통로 개설 (유연성 확보)
    public void setNavigationController(NavigationController navController) {
        this.navController = navController;
    }

    // SpaceRepository의 실제 전용 조회 함수 매핑 적용
    private void updateTableData(String type) {
        tableModel.setRowCount(0);
        try {
            if (type.equals("SEAT")) {
                this.spaceList = spaceRepo.findAllSeats();
                for (Space space : spaceList) {
                    tableModel.addRow(new Object[]{space.getSpaceId(), space.getName(), space.getLocation()});
                }
            } else if (type.equals("STUDY_ROOM")) {
                this.spaceList = spaceRepo.findAllStudyRooms();
                for (Space space : spaceList) {
                    tableModel.addRow(new Object[]{space.getSpaceId(), space.getName(), space.getLocation()});
                }
            }
        } catch (Exception e) {
            this.spaceList = new ArrayList<>();
        }
    }

    private Space findSpaceById(String id) {
        if (spaceList == null) return null;
        for (Space space : spaceList) {
            if (space.getSpaceId().equals(id)) {
                return space;
            }
        }
        return null;
    }
}
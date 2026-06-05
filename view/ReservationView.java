package view;

import controller.NavigationController;
import controller.ReservationController;
import model.Seat;
import model.StudyRoom;
import model.Space;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationView extends JPanel {
    private NavigationController navController;
    private ReservationController resController;

    // UI 컴포넌트
    private JRadioButton seatRadio;
    private JRadioButton studyRoomRadio;
    private JTable spaceTable;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> datePicker;
    private JComboBox<String> startTimePicker;
    private JComboBox<String> endTimePicker;
    
    // 스터디룸 전용 인원수 입력 패널 및 필드
    private JPanel personnelPanel;
    private JTextField personnelField;
    private JButton reserveButton;

    // 가상 데이터 리스트 (실제 환경에서는 SpaceRepository 등에서 받아옴)
    private List<Space> spaceList;

    public ReservationView(NavigationController navController, ReservationController resController) {
        this.navController = navController;
        this.resController = resController;
        
        // 더미 데이터 초기화 (검증을 위해 최소/최대 인원 세팅)
        initDummySpaces();
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---------------------------------------------------------------
        // 1. 상단 타이틀
        // ---------------------------------------------------------------
        JLabel titleLabel = new JLabel("예약하기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // ---------------------------------------------------------------
        // 2. 중앙 메인 컨텐츠 영역
        // ---------------------------------------------------------------
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));

        // [왼쪽] 1. 공간 타입 선택
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

        // [오른쪽] 2. 세부 공간 목록 테이블
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("2. 공간 선택"));

        String[] columnNames = {"공간 ID", "공간명", "위치"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 더블클릭 시 텍스트 수정 방지
            }
        };
        
        spaceTable = new JTable(tableModel);
        spaceTable.setRowHeight(25);
        spaceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //한번 클릭 시 선택, 더블 클릭 시 상세 정보창 전환
        spaceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = spaceTable.getSelectedRow();
                if (selectedRow == -1) return;

                // 더블 클릭 감지 (clickCount == 2)
                if (e.getClickCount() == 2) {
                    String spaceId = (String) spaceTable.getValueAt(selectedRow, 0);
    
   
                    SpaceDetailView detailView = (SpaceDetailView) navController.getView("SPACE_DETAIL");
                    if (detailView != null) {
                        detailView.updateDetailData(spaceId); // 상대방이 만든 업데이트 메서드 호출
                    }
    
                    // 그 후 화면 전환
                    navController.navigateTo("SPACE_DETAIL"); 
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(spaceTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ---------------------------------------------------------------
        // 3. 하단 설정 및 예약 신청 영역
        // ---------------------------------------------------------------
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        
        // 시간 및 인원수 설정 영역 (GridBagLayout으로 정밀 정렬)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 3. 시작 시간
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("3. 시작 시간"), gbc);
        
        gbc.gridx = 1;
        datePicker = new JComboBox<>(new String[]{LocalDate.now().toString()});
        formPanel.add(datePicker, gbc);
        
        gbc.gridx = 2;
        startTimePicker = new JComboBox<>(new String[]{"09:00", "11:00", "14:00", "16:00", "18:00"});
        formPanel.add(startTimePicker, gbc);

        // 4. 종료 시간
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("4. 종료 시간"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> endDatePicker = new JComboBox<>(new String[]{LocalDate.now().toString()});
        formPanel.add(endDatePicker, gbc);
        
        gbc.gridx = 2;
        endTimePicker = new JComboBox<>(new String[]{"11:00", "13:00", "16:00", "18:00", "20:00"});
        formPanel.add(endTimePicker, gbc);

        // 5. 스터디룸 예약 시 인원 수 추가 입력 칸 생성
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel personnelLabel = new JLabel("5. 인원 수 입력");
        formPanel.add(personnelLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        personnelField = new JTextField(10);
        formPanel.add(personnelField, gbc);

        // 기본적으로 첫 화면은 '좌석'이 선택되어 있으므로 인원수 입력 필드는 숨김/비활성화 처리
        personnelLabel.setVisible(false);
        personnelField.setVisible(false);

        bottomPanel.add(formPanel, BorderLayout.WEST);

        // 라디오 버튼 전환 이벤트: 스터디룸 클릭할 때만 인원 수 입력 창 등장
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

        // 최종 안내 라벨 및 예약 버튼 패널
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

        // 초기 테이블 데이터 로드 (좌석 먼저 노출)
        updateTableData("SEAT");

        // ---------------------------------------------------------------
        //  예약 기능 구현 및 인원수 유효성 검증
        // ---------------------------------------------------------------
        reserveButton.addActionListener(e -> {
            int selectedRow = spaceTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "예약할 공간을 목록에서 먼저 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String spaceId = (String) spaceTable.getValueAt(selectedRow, 0);
            Space selectedSpace = findSpaceById(spaceId);

            // 스터디룸 조건 검증 진행
            if (studyRoomRadio.isSelected() && selectedSpace instanceof StudyRoom) {
                StudyRoom room = (StudyRoom) selectedSpace;
                String inputStr = personnelField.getText().trim();

                if (inputStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "스터디룸은 이용 인원수를 반드시 입력해야 합니다.", "검증 실패", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int pCount = Integer.parseInt(inputStr);

                    //  스터디룸 모델의 최소인원 및 수용가능인원 범위 조건 체크
                    if (pCount < room.getMinCapacity() || pCount > room.getMaxCapacity()) {
                        String errMsg = String.format("해당 스터디룸의 이용 가능 인원은 [%d명 ~ %d명] 입니다.\n입력하신 인원(%d명)은 예약이 불가능합니다.",
                                room.getMinCapacity(), room.getMaxCapacity(), pCount);
                        JOptionPane.showMessageDialog(this, errMsg, "인원수 초과/미달", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "인원 수는 숫자만 입력할 수 있습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 모든 검증 통과 시 예약 성공 루틴
            JOptionPane.showMessageDialog(this, selectedSpace.getName() + " 예약이 성공적으로 완료되었습니다!");
            navController.navigateTo("MY_RESERVATION"); // 예약 현황판 화면으로 리다이렉트
        });
    }

    // 데이터 교체 함수
    private void updateTableData(String type) {
        tableModel.setRowCount(0);
        for (Space space : spaceList) {
            if (type.equals("SEAT") && space instanceof Seat) {
                tableModel.addRow(new Object[]{space.getId(), space.getName(), space.getLocation()});
            } else if (type.equals("STUDY_ROOM") && space instanceof StudyRoom) {
                tableModel.addRow(new Object[]{space.getId(), space.getName(), space.getLocation()});
            }
        }
    }

    private Space findSpaceById(String id) {
        for (Space space : spaceList) {
            if (space.getId().equals(id)) return space;
        }
        return null;
    }

    // 가상 모델 클래스 연동용 더미 데이터 설정
    private void initDummySpaces() {
        spaceList = new ArrayList<>();
        // Seat 더미 객체 (ID, 이름, 위치, 수용인원 등)
        spaceList.add(new Seat("S001", "1층 창가 좌석", "중앙도서관 1층"));
        spaceList.add(new Seat("S002", "2층 조용한 좌석", "중앙도서관 2층"));
        spaceList.add(new Seat("S003", "3층 집중 좌석", "중앙도서관 3층"));
        
        // StudyRoom 더미 객체 (ID, 이름, 위치, 수용인원, 최소인원, 최대인원 설정 가상)
        // 가정: StudyRoom(id, name, location, capacity, minCapacity, maxCapacity)
        spaceList.add(new StudyRoom("R001", "그룹 스터디룸 A (4인)", "중앙도서관 3층", 4, 2, 4));
        spaceList.add(new StudyRoom("R002", "그룹 스터디룸 B (6인)", "중앙도서관 3층", 6, 4, 6));
    }
}
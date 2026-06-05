package view;

import controller.NavigationController;
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
public class MyReservationView extends BaseView {

    private final MyReservationController myReservationController;
    private final User currentUser;

    private JTable activeTable;
    private JTable pastTable;

    private DefaultTableModel activeModel;
    private DefaultTableModel pastModel;

    private JButton refreshButton;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 
     */
    public MyReservationView(
            NavigationController navController,
            MyReservationController myReservationController,
            User currentUser
    ) {
        // 부모 클래스인 BaseView의 생성자 호출 (자동으로 레이아웃 세팅 및 네비게이션 바 부착)
        super(navController); 

        this.myReservationController = myReservationController;
        this.currentUser = currentUser;

        // BaseView가 자동으로 레이아웃을 BorderLayout으로 만들므로 내부 UI 요소들만 재배치합니다.
        initUI();

        // 초기 데이터 로드
        loadReservationData();
    }

    /**
     * UI 구성 
     */
    private void initUI() {
        // BaseView의 기본 레이아웃인 BorderLayout 구조를 그대로 활용합니다.
        

        // 1. 상단 패널 (사용자 정보 및 새로고침 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(VIEW_BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel userLabel = new JLabel(
                currentUser.getName() + " (" + currentUser.getStudentId() + ")님의 예약 현황"
        );
        userLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        refreshButton = new JButton("새로고침");
        refreshButton.setFont(BODY_FONT);

        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        // BaseView 상단에 네비게이션 바가 이미 붙어 있으므로, topPanel은 CENTER 패널의 상단에 배치되도록 감싸줍니다.
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(VIEW_BG_COLOR);
        mainContentPanel.add(topPanel, BorderLayout.NORTH);

        // 2. 중앙 테이블 영역 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(VIEW_BG_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        String[] columns = {
                "예약 ID",
                "공간 ID",
                "시작 시간",
                "종료 시간",
                "상태"
        };

        // --- 현재 예약 섹션 ---
        JLabel activeLabel = new JLabel("▶ 현재 / 대기 중인 예약");
        activeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        activeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        activeModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 더블클릭 수정 금지
            }
        };
        activeTable = new JTable(activeModel);
        activeTable.setFont(BODY_FONT);
        activeTable.setRowHeight(22);
        JScrollPane activeScroll = new JScrollPane(activeTable);
        activeScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- 지난 예약 섹션 ---
        JLabel pastLabel = new JLabel("▶ 지난 예약 내역");
        pastLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        pastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pastModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pastTable = new JTable(pastModel);
        pastTable.setFont(BODY_FONT);
        pastTable.setRowHeight(22);
        JScrollPane pastScroll = new JScrollPane(pastTable);
        pastScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 패널에 차곡차곡 추가
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(activeLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(activeScroll);

        centerPanel.add(Box.createVerticalStrut(25));

        centerPanel.add(pastLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(pastScroll);

        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        // 완성된 전체 컨텐츠 패널을 부모 BaseView의 CENTER 영역에 부착합니다.
        add(mainContentPanel, BorderLayout.CENTER);

        // 새로고침 이벤트 연결
        refreshButton.addActionListener(e -> loadReservationData());
    }

    /**
     * 예약 데이터 로드 및 테이블 초기화
     */
    private void loadReservationData() {
        activeModel.setRowCount(0);
        pastModel.setRowCount(0);

       

        String studentId = currentUser.getStudentId();

        // 1. 현재 예약 리스트 출력
        List<Reservation> activeReservations = 
                myReservationController.getActiveReservations(studentId);

        for (Reservation reservation : activeReservations) {
            
            String statusText = "이용 중";
            if (reservation.isUpcoming()) {
                statusText = "이용 대기";
            }

            activeModel.addRow(new Object[]{
                    reservation.getReservationId(),
                    reservation.getSpaceId(),
                    reservation.getStartTime().format(formatter),
                    reservation.getEndTime().format(formatter),
                    statusText // 문자열 할당 완료
            });
        }

        // 2. 지난 예약 리스트 출력
        List<Reservation> pastReservations = 
                myReservationController.getPastReservations(studentId);

        for (Reservation reservation : pastReservations) {
            // 지난 예약은 항상 완료 상태이므로 고정 텍스트 할당
            String statusText = "이용 완료";

            pastModel.addRow(new Object[]{
                    reservation.getReservationId(),
                    reservation.getSpaceId(),
                    reservation.getStartTime().format(formatter),
                    reservation.getEndTime().format(formatter),
                    statusText
            });
        }
    }

    /**
     * BaseView 추상 메서드 의무 구현
     * 
     */
    @Override
    protected JPanel createContent() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BorderLayout());
        return emptyPanel;
    }
}
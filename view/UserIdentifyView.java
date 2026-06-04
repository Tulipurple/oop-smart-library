package view;

import controller.NavigationController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 *  사용자 식별 및 로그인
 */
public class UserIdentifyView extends BaseView {

    private final UserController userController;
    
    private JTextField studentIdField;
    private JTextField nameField;
    private JButton startButton;

    /**
     * 생성자
     * @param navController 시스템의 화면 전환을 제어하는 컨트롤러
     * @param userController 사용자 데이터 처리를 위한 컨트롤러
     */
    public UserIdentifyView(NavigationController navController, UserController userController) {
        super(navController);
        this.userController = userController;
    }

    /**
     * 화면의 JPanel 콘텐츠를 생성하는 메서드
     */
    @Override
    protected JPanel createContent() {
        // 단일 패널에 절대 좌표(null) 레이아웃 적용
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // 1. 상단 시스템 타이틀 (HomeView의 디자인 컴포넌트 크기 계승)
        JLabel titleLabel = new JLabel("도서관 예약 시스템");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(50, 60, 700, 50);
        panel.add(titleLabel);

        // 2. 입력 폼을 감싸는 정갈한 안내 패널 (HomeView의 가로세로 비율 맞춤)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBounds(150, 160, 500, 180);

        Font labelFont = new Font("맑은 고딕", Font.BOLD, 16);

        // 학번 입력 라벨 & 필드
        JLabel idLabel = new JLabel("학번");
        idLabel.setFont(labelFont);
        idLabel.setBounds(60, 45, 80, 30);
        formPanel.add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        studentIdField.setBounds(150, 45, 280, 30);
        formPanel.add(studentIdField);

        // 이름 입력 라벨 & 필드
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(60, 105, 80, 30);
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        nameField.setBounds(150, 105, 280, 30);
        formPanel.add(nameField);

        panel.add(formPanel);

        // 3. 시스템 시작 (로그인) 버튼
        startButton = new JButton("시스템 시작하기");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        startButton.setBounds(300, 390, 200, 45);
        panel.add(startButton);

        // --- 이벤트 연결 (람다 표준식) ---
        startButton.addActionListener(e -> handleLoginOrRegister());

        return panel;
    }

    /**
     * 로그인/식별 버튼 클릭 시 처리 로직
     */
    private void handleLoginOrRegister() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();

        // 1. 공백 유효성 검증
        if (studentId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "학번과 이름을 모두 입력해 주세요.", 
                    "알림", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2.유저 인증 로직 수행
        User user = userController.loginOrRegister(studentId, name);

        if (user != null) {
            JOptionPane.showMessageDialog(null, 
                    user.getName() + "님, 인증에 성공하였습니다.", 
                    "로그인 성공", 
                    JOptionPane.INFORMATION_MESSAGE);
             
            //내비게이션 기능으로 다음 화면인 "HOME" 화면으로 유저 정보를 넘기며 전환
            getNavController().navigateTo("HOME", user); 
            
        } else {
            JOptionPane.showMessageDialog(null, 
                    "입력 정보가 올바르지 않거나 중복된 학번입니다.", 
                    "로그인 실패", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
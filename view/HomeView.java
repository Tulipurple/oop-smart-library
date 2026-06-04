package view;

import controller.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomeView extends BaseView {

    private JButton startTestButton;

    public HomeView(NavigationController navController) {
        super(navController);
    }

    @Override
    protected JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // 상단 시스템 타이틀
        JLabel titleLabel = new JLabel("OOP Smart Library");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(50, 60, 700, 50);
        panel.add(titleLabel);

        // 안내 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBounds(150, 160, 500, 160);

        JLabel infoLabel1 = new JLabel("성향 기반 좌석 추천과");
        infoLabel1.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        infoLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel1.setBounds(50, 45, 400, 30);
        infoPanel.add(infoLabel1);

        JLabel infoLabel2 = new JLabel("예약 관리를 제공하는 도서관 시스템입니다.");
        infoLabel2.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel2.setBounds(50, 85, 400, 30);
        infoPanel.add(infoLabel2);

        panel.add(infoPanel);

        // 좌석 추천 시작 버튼
        startTestButton = new JButton("좌석 추천 시작하기");
        startTestButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        startTestButton.setBounds(300, 380, 200, 45);
        panel.add(startTestButton);

        return panel;
    }

    // 좌석 추천 시작 버튼 리스너 등록
    public void addStartTestButtonListener(ActionListener listener) {
        startTestButton.addActionListener(listener);
    }
}
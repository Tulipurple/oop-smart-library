package view;

import controller.NavigationController;

import javax.swing.*;
import java.awt.*;

/**
 * 모든 View 화면의 공통 부모 추상 클래스
 */
public abstract class BaseView extends JPanel {

    protected static final Color NAV_BG_COLOR =
        new Color(44, 62, 80);

    protected static final Color NAV_TEXT_COLOR =
        Color.WHITE;

    protected static final Color VIEW_BG_COLOR =
        new Color(245, 246, 250);

    protected static final Font TITLE_FONT =
        new Font("맑은 고딕", Font.BOLD, 22);

    protected static final Font BODY_FONT =
        new Font("맑은 고딕", Font.PLAIN, 14);

    protected static final Font NAV_FONT =
        new Font("맑은 고딕", Font.BOLD, 13);

    protected final NavigationController navController;

    public BaseView(NavigationController navController) {

        this.navController = navController;

        setLayout(new BorderLayout());
        setBackground(VIEW_BG_COLOR);

        add(buildNavBar(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    /**
     * 화면별 콘텐츠 생성
     */
    protected abstract JPanel createContent();

    /**
     * 화면 진입 시 호출
     */
    public void onShow() {
    }

    /**
     * 데이터 전달 화면 진입 시 호출
     */
    public void onShow(Object data) {
        onShow();
    }

    /**
     * 공통 네비게이션 바
     */
    private JPanel buildNavBar() {

        JPanel nav =
            new JPanel(new FlowLayout(
                FlowLayout.LEFT, 12, 10
            ));

        nav.setBackground(NAV_BG_COLOR);
        nav.setPreferredSize(
            new Dimension(900, 50)
        );

        nav.add(navButton(
            "홈",
            NavigationController.HOME
        ));

        nav.add(navButton(
            "예약 현황",
            NavigationController.MY_RESERVATION
        ));

        nav.add(navButton(
            "예약하기",
            NavigationController.RESERVATION
        ));

        nav.add(navButton(
            "좌석 추천",
            NavigationController.TEST
        ));

        nav.add(navButton(
            "후기/상태 등록",
            NavigationController.SPACE_FORM
        ));

        return nav;
    }

    /**
     * 네비게이션 버튼 생성
     */
    private JButton navButton(
        String text,
        String target
    ) {

        JButton btn = new JButton(text);

        btn.setFont(NAV_FONT);
        btn.setForeground(NAV_TEXT_COLOR);
        btn.setBackground(NAV_BG_COLOR);

        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        btn.addActionListener(
            e -> navController.navigateTo(target)
        );

        return btn;
    }
}
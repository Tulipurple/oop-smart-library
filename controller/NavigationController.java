package controller;

import view.BaseView;

import javax.swing.*;
import java.awt.CardLayout;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * CardLayout 기반 전체 화면 전환 흐름 제어 컨트롤러
 */
public class NavigationController {

    public static final String HOME           = "HOME";
    public static final String MY_RESERVATION = "MY_RESERVATION";
    public static final String RESERVATION    = "RESERVATION";
    public static final String TEST           = "TEST";
    public static final String TEST_RESULT    = "TEST_RESULT";
    public static final String RECOMMEND      = "RECOMMEND";
    public static final String SPACE_DETAIL   = "SPACE_DETAIL";
    public static final String SPACE_FORM     = "SPACE_FORM";
    public static final String LOGIN          = "LOGIN";

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Map<String, BaseView> viewMap;
    private final Deque<String> history;

    private String currentViewName;

    public NavigationController(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.viewMap = new HashMap<>();
        this.history = new ArrayDeque<>();
    }

    /**
     * View 등록
     */
    public void register(String name, BaseView view) {

        if (name == null || view == null) {
            throw new IllegalArgumentException(
                "화면 이름 또는 View가 null입니다."
            );
        }

        if (viewMap.containsKey(name)) {
            throw new IllegalArgumentException(
                "이미 등록된 화면입니다: " + name
            );
        }

        viewMap.put(name, view);
        mainPanel.add(view, name);
    }

    /**
     * 초기 화면 표시
     */
    public void showInitial(String name) {
        showView(name, null);
    }

    /**
     * 일반 화면 이동
     */
    public void navigateTo(String name) {
        pushHistory();
        showView(name, null);
    }

    /**
     * 데이터 전달 화면 이동
     */
    public void navigateTo(String name, Object data) {
        pushHistory();
        showView(name, data);
    }

    /**
     * 뒤로 가기
     */
    public void navigateBack() {

        if (history.isEmpty()) {
            return;
        }

        String previous = history.pop();

        cardLayout.show(mainPanel, previous);
        currentViewName = previous;

        try {
            viewMap.get(previous).onShow();
        } catch (Exception e) {
            System.err.println(
                "[NavigationController] navigateBack() 실패 - "
                + previous
            );
            e.printStackTrace();
        }
    }

    /**
     * 현재 화면 반환
     */
    public String getCurrentViewName() {
        return currentViewName;
    }

    /**
     * 화면 등록 여부 확인
     */
    public boolean isRegistered(String name) {
        return viewMap.containsKey(name);
    }

    /**
     * 현재 화면 히스토리 저장
     */
    private void pushHistory() {

        if (currentViewName != null) {
            history.push(currentViewName);
        }
    }

    /**
     * 실제 화면 전환
     */
    private void showView(String name, Object data) {

        if (!viewMap.containsKey(name)) {
            throw new IllegalArgumentException(
                "등록되지 않은 화면입니다: " + name
            );
        }

        cardLayout.show(mainPanel, name);
        currentViewName = name;

        try {

            if (data != null) {
                viewMap.get(name).onShow(data);
            } else {
                viewMap.get(name).onShow();
            }

        } catch (Exception e) {

            System.err.println(
                "[NavigationController] onShow() 실패 - "
                + name
            );

            e.printStackTrace();
        }
    }
}
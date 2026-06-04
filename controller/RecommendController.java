package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.PreferenceResult;
import model.Recommender;
import model.Space;
import model.User;
import view.RecommendView;

public class RecommendController {

    private final Recommender recommender;
    private final PreferenceResult resultModel;
    private final RecommendView view;
    private final User currentUser;

    public RecommendController(Recommender recommender,
                               PreferenceResult resultModel,
                               RecommendView view,
                               User currentUser) {
        this.recommender = recommender;
        this.resultModel = resultModel;
        this.view = view;
        this.currentUser = currentUser;

        initController();
    }

    // 버튼 액션 리스너 이벤트 초기화
    private void initController() {
        view.addRecommendButtonListener(new RecommendActionClickListener());
    }

    // 알고리즘 스펙 연산 실행 및 최종 결과 뷰 바인딩 요청
    private void executeRecommendation() {
        List<Space> recommendedSpaces =
                recommender.recommendSpaces(resultModel);

        view.displayRecommendations(
                currentUser.getName(),
                recommendedSpaces);
    }

    // 추천 매칭 실행 명령 핸들링을 위한 이벤트 리스너
    private class RecommendActionClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            executeRecommendation();
        }
    }
}
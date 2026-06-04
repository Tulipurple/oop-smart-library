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
    private TestController testController;

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

    public void setTestController(TestController testController) {
        this.testController = testController;
    }

    private void initController() {
        view.addRecommendButtonListener(
                new RecommendActionClickListener()
        );
    }

    private void executeRecommendation() {

        System.out.println("===== 추천 시작 =====");

        System.out.println(
                "quiet = "
                        + resultModel.getQuietPreference()
        );

        System.out.println(
                "conversation = "
                        + resultModel.getConversationPreference()
        );

        System.out.println(
                "typing = "
                        + resultModel.getTypingNeed()
        );

        System.out.println(
                "open = "
                        + resultModel.getOpenPreference()
        );

        System.out.println(
                "partition = "
                        + resultModel.getPartitionPreference()
        );

        System.out.println(
                "socket = "
                        + resultModel.getSocketNeed()
        );

        List<Space> recommendedSpaces =
                recommender.recommendSpaces(resultModel);

        view.displayRecommendations(
                currentUser.getName(),
                recommendedSpaces
        );
    }

    public void refreshRecommendations() {
        executeRecommendation();
    }

    private class RecommendActionClickListener
            implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (testController != null) {
                testController.resetTest();
            }

            view.navigateToTest();
        }
    }
}
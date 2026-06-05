import controller.MyReservationController;
import controller.NavigationController;
import controller.RecommendController;
import controller.ReservationController;
import controller.SpaceController;
import controller.SpaceFormController;
import controller.TestController;
import controller.UserController;
import model.PreferenceResult;
import model.PreferenceTest;
import model.Recommender;
import model.User;
import repository.ReservationRepository;
import repository.ReviewRepository;
import repository.SpaceRepository;
import repository.StatusRepository;
import view.BaseView;
import view.HomeView;
import view.MyReservationView;
import view.RecommendView;
import view.ReservationView;
import view.SpaceDetailView;
import view.SpaceFormView;
import view.TestView;
import view.UserIdentifyView;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::launch);
    }

    private static void launch() {
        JFrame frame = new JFrame("OOP Smart Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        NavigationController navController =
                new NavigationController(cardLayout, mainPanel);

        // 공유 Repository
        SpaceRepository spaceRepo     = new SpaceRepository();
        ReviewRepository reviewRepo   = new ReviewRepository();
        StatusRepository statusRepo   = new StatusRepository();
        ReservationRepository reservationRepo = new ReservationRepository();


        // 공유 모델
        PreferenceTest   preferenceTest   = new PreferenceTest();
        PreferenceResult preferenceResult = new PreferenceResult();
        Recommender      recommender      = new Recommender(spaceRepo);

        // User Controller
        UserController     userController     = new UserController();
        SpaceFormController spaceFormController =
                new SpaceFormController(reviewRepo, statusRepo, spaceRepo);
        SpaceController    spaceController    =
                new SpaceController(spaceRepo, reviewRepo, statusRepo);

        // User View
        HomeView       homeView       = new HomeView(navController);
        TestView       testView       = new TestView(navController);
        RecommendView  recommendView  = new RecommendView(navController);
        SpaceDetailView spaceDetailView = new SpaceDetailView(navController);
        SpaceFormView  spaceFormView  =
                new SpaceFormView(navController, spaceFormController);

        // 순환 참조 해소
        spaceController.setView(spaceDetailView);
        spaceDetailView.setController(spaceController);
        spaceFormController.setView(spaceFormView);

        // TestController
        TestController testController =
                new TestController(preferenceTest, preferenceResult, testView);

        // View 등록 (UserIdentifyView는 로그인 성공 후 등록)
        navController.register(NavigationController.HOME,         homeView);
        navController.register(NavigationController.TEST,         testView);
        navController.register(NavigationController.RECOMMEND,    recommendView);
        navController.register(NavigationController.SPACE_DETAIL, spaceDetailView);
        navController.register(NavigationController.SPACE_FORM,   spaceFormView);

        // 로그인 화면
        UserIdentifyView loginView =
                new UserIdentifyView(navController, userController);

        loginView.setOnLoginSuccess(user -> {
            // 최초 로그인 시 1회만 등록
            if (navController.isRegistered(NavigationController.MY_RESERVATION)) {
                return;
            }

            // 예약 현황 (User 객체 필요)
            MyReservationController myResCtrl = new MyReservationController(reservationRepo);
            MyReservationView myResView =
                    new MyReservationView(navController, myResCtrl, user);
            navController.register(NavigationController.MY_RESERVATION, myResView);

            // 예약하기 (User 객체 필요)
<<<<<<< Updated upstream
            ReservationController resCtrl = new ReservationController();
=======
            ReservationController resCtrl = new ReservationController(reservationRepo);
            ReservationView reservationPanel = new ReservationView(resCtrl, user);
            reservationPanel.setNavigationController(navController);
>>>>>>> Stashed changes
            BaseView reservationAdapter = new BaseView(navController) {
                @Override
                protected JPanel createContent() {
                    return new JPanel();
                }
                @Override
                public void onShow() {
                    navController.navigateBack();
                    SwingUtilities.invokeLater(() ->
                            new ReservationView(resCtrl, user).setVisible(true));
                }
            };
            navController.register(NavigationController.RESERVATION, reservationAdapter);

            // 추천 컨트롤러 (User 객체 필요)
            RecommendController recommendController =
                    new RecommendController(recommender, preferenceResult, recommendView, user);
            recommendController.setTestController(testController);
            recommendView.setController(recommendController);
        });

        navController.register(NavigationController.LOGIN, loginView);

        // 앱 시작 - 로그인 화면
        navController.showInitial(NavigationController.LOGIN);
        frame.setVisible(true);
    }
}

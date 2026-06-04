package controller;

import java.util.List;

import model.Review;
import model.Space;
import model.Status;
import repository.ReviewRepository;
import repository.SpaceRepository;
import repository.StatusRepository;
import view.SpaceDetailView;

public class SpaceController {

    private final SpaceRepository spaceRepo;
    private final ReviewRepository reviewRepo;
    private final StatusRepository statusRepo;
    private SpaceDetailView view;

    public SpaceController(SpaceRepository spaceRepo,
                           ReviewRepository reviewRepo,
                           StatusRepository statusRepo) {
        this.spaceRepo = spaceRepo;
        this.reviewRepo = reviewRepo;
        this.statusRepo = statusRepo;
    }

    public void setView(SpaceDetailView view) {
        this.view = view;
    }

    public void loadSpaceDetails(Space space) {
        if (space == null || view == null) {
            return;
        }

        String spaceId = space.getSpaceId();

        List<Review> reviews =
                reviewRepo.findBySpaceId(spaceId);

        List<Status> statuses =
                statusRepo.findBySpaceId(spaceId, 3);

        double avgNoise = 0.0;
        double avgCrowd = 0.0;
        double avgClean = 0.0;

        if (statuses.size() > 0) {
            int sumNoise = 0;
            int sumCrowd = 0;
            int sumClean = 0;

            for (int i = 0; i < statuses.size(); i++) {
                Status status = statuses.get(i);

                sumNoise += status.getNoiseLevel();
                sumCrowd += status.getCrowdedness();
                sumClean += status.getCleanliness();
            }

            avgNoise = (double) sumNoise / statuses.size();
            avgCrowd = (double) sumCrowd / statuses.size();
            avgClean = (double) sumClean / statuses.size();
        }

        view.updateSpaceDetails(
                space,
                avgNoise,
                avgCrowd,
                avgClean,
                reviews
        );
    }
}
package model;

import repository.SpaceRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recommender {

    private final SpaceRepository spaceRepository;

    public Recommender(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public List<Space> recommendSpaces(PreferenceResult result) {

        List<Space> allSeats =
                spaceRepository.findAllSeats();

        List<ScoredSpace> scoredSpaces =
                new ArrayList<>();

        for (Space space : allSeats) {

            if (space instanceof Seat) {

                Seat seat = (Seat) space;

                double score =
                        calculateScore(result, seat);

                scoredSpaces.add(
                        new ScoredSpace(seat, score)
                );
            }
        }

        Collections.sort(scoredSpaces);

        List<Space> recommended =
                new ArrayList<>();

        List<String> usedLocations =
                new ArrayList<>();

        for (ScoredSpace scored : scoredSpaces) {

            String location =
                    scored.getSpace().getLocation();

            if (usedLocations.contains(location)) {
                continue;
            }

            recommended.add(
                    scored.getSpace()
            );

            usedLocations.add(location);

            if (recommended.size() == 3) {
                break;
            }
        }

        return recommended;
    }

    private double calculateScore(
            PreferenceResult result,
            Seat seat
    ) {

        double score = 0;

        score += result.getQuietPreference()
                * seat.getQuietLevel();

        score += result.getConversationPreference()
                * seat.getConversationLevel();

        score += result.getPartitionPreference()
                * seat.getPartitionLevel();

        score += result.getOpenPreference()
                * seat.getOpenLevel();

        score += result.getTypingNeed()
                * seat.getTypingLevel();

        score += result.getSocketNeed()
                * seat.getSocketLevel();

        score += result.getSofaPreference()
                * seat.getSofaLevel();

        score += result.getRollingChairPreference()
                * seat.getRollingChairLevel();

        score += result.getNormalChairPreference()
                * seat.getNormalChairLevel();

        return score;
    }

    private static class ScoredSpace
            implements Comparable<ScoredSpace> {

        private final Seat seat;
        private final double score;

        public ScoredSpace(
                Seat seat,
                double score
        ) {
            this.seat = seat;
            this.score = score;
        }

        public Seat getSpace() {
            return seat;
        }

        @Override
        public int compareTo(
                ScoredSpace other
        ) {
            return Double.compare(
                    other.score,
                    this.score
            );
        }
    }
}
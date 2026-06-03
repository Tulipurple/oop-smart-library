package repository;

import model.Status;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatusRepository extends BaseRepository<Status> {

    private static final String FILE_PATH = "data/statuses.csv";

    public StatusRepository() {
        super(FILE_PATH);
    }

    // ───────────────────────────────────────────────────────
    // 파일 I/O
    // ───────────────────────────────────────────────────────

    @Override
    protected void loadFromFile() {
        for (String line : readLines()) {
            try {
                dataList.add(Status.fromCsvLine(line));
            } catch (IllegalArgumentException e) {
                System.err.println("상태 데이터 로드 실패: " + e.getMessage());
            }
        }
    }

    @Override
    protected String toCsvLine(Status status) {
        return status.toCsvLine();
    }

    // ───────────────────────────────────────────────────────
    // 조회 메서드
    // ───────────────────────────────────────────────────────

    // 공간 ID로 상태 조회 (최신 순 정렬)
    public List<Status> findBySpaceId(String spaceId, int limit) {
        List<Status> result = findBy(s -> s.getSpaceId().equals(spaceId));
        Collections.sort(result, Comparator.comparing(Status::getCreatedAt).reversed());
        return result.subList(0, Math.min(limit, result.size()));
    }
}

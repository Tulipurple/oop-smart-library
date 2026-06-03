package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 상태 클래스
 */
public class Status {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final String statusId; // 상태 ID
    private final String spaceId; // 공간 ID
    private final int noiseLevel; // 소음정도 (1~5)
    private final int crowdedness; // 혼잡도 (1~5)
    private final int cleanliness; // 청결도 (1~5)
    private final LocalDateTime createdAt; // 작성 시간

    // 생성자
    public Status(String statusId, String spaceId, int noiseLevel, int crowdedness, int cleanliness, LocalDateTime createdAt) {
        if (statusId == null || statusId.isBlank()) throw new IllegalArgumentException("statusId는 비어있을 수 없습니다.");
        if (spaceId  == null || spaceId.isBlank())  throw new IllegalArgumentException("spaceId는 비어있을 수 없습니다.");
        if (noiseLevel  < 1 || noiseLevel  > 5)     throw new IllegalArgumentException("noiseLevel은 1~5 사이여야 합니다: " + noiseLevel);
        if (crowdedness < 1 || crowdedness > 5)      throw new IllegalArgumentException("crowdedness는 1~5 사이여야 합니다: " + crowdedness);
        if (cleanliness < 1 || cleanliness > 5)      throw new IllegalArgumentException("cleanliness는 1~5 사이여야 합니다: " + cleanliness);
        this.statusId = statusId;
        this.spaceId = spaceId;
        this.noiseLevel = noiseLevel;
        this.crowdedness = crowdedness;
        this.cleanliness = cleanliness;
        this.createdAt = createdAt;
    }

    // ───────────────────────────────────────────────────────
    // 직렬화 / 역직렬화
    // ───────────────────────────────────────────────────────

    // 객체 -> CSV 라인
    // CSV 저장 형식: statusId,spaceId,noiseLevel,crowdedness,cleanliness,createdAt
    public String toCsvLine() {
        return String.format("%s,%s,%d,%d,%d,%s",
        statusId, spaceId, noiseLevel, crowdedness, cleanliness, createdAt.format(FORMATTER));
    }

    // CSV 라인 -> 객체
    public static Status fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("잘못된 Status 형식: " + csvLine);
        }
        String statusId = parts[0];
        String spaceId = parts[1];
        int noiseLevel = Integer.parseInt(parts[2]);
        int crowdedness = Integer.parseInt(parts[3]);
        int cleanliness = Integer.parseInt(parts[4]);
        LocalDateTime createdAt = LocalDateTime.parse(parts[5], FORMATTER);
        return new Status(statusId, spaceId, noiseLevel, crowdedness, cleanliness, createdAt);
    }

    // ───────────────────────────────────────────────────────
    // Getter
    // ───────────────────────────────────────────────────────

    public String getStatusId() { return statusId;}
    public String getSpaceId() { return spaceId;}
    public int getNoiseLevel() { return noiseLevel;}
    public int getCrowdedness() { return crowdedness;}
    public int getCleanliness() { return cleanliness;}
    public LocalDateTime getCreatedAt() { return createdAt;}

}

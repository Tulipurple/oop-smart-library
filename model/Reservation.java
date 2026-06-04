package model;

import java.time.LocalDateTime;

/**
 * 예약 정보 모델
 * 일반 좌석(SEAT)은 인원수가 1로 고정되며,
 * 스터디룸(STUDY_ROOM)은 입력받은 인원수를 관리한다.
 */
public class Reservation {

    private final String reservationId;     // 예약 ID
    private final String studentId;         // 예약자 학번
    private final String spaceId;           // 공간 ID
    private final int personCount;          // 이용 인원수

    private final LocalDateTime startTime;  // 시작 시간
    private final LocalDateTime endTime;    // 종료 시간

    /**
     * 예약 생성자
     */
    public Reservation(String reservationId,
                       String studentId,
                       String spaceId,
                       int personCount,
                       LocalDateTime startTime,
                       LocalDateTime endTime) {

        if (personCount < 1) {
            throw new IllegalArgumentException("인원수는 1명 이상이어야 합니다.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        this.reservationId = reservationId;
        this.studentId = studentId;
        this.spaceId = spaceId;
        this.personCount = personCount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // ==========================
    // Getter
    // ==========================

    public String getReservationId() {
        return reservationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public int getPersonCount() {
        return personCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // ==========================
    // 예약 상태 판별
    // ==========================

    /**
     * 현재 사용 중인 예약
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();

        return !now.isBefore(startTime)
                && !now.isAfter(endTime);
    }

    /**
     * 완료된 예약
     */
    public boolean isCompleted() {
        return LocalDateTime.now().isAfter(endTime);
    }

    /**
     * 시작 전 예약
     */
    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startTime);
    }

    // ==========================
    // CSV 저장
    // ==========================

    /**
     * 형식:
     * reservationId,studentId,spaceId,personCount,startTime,endTime
     */
    public String toCsvLine() {
        return reservationId + ","
                + studentId + ","
                + spaceId + ","
                + personCount + ","
                + startTime + ","
                + endTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", spaceId='" + spaceId + '\'' +
                ", personCount=" + personCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
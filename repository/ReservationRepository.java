package repository;

import model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 데이터를 관리하는 Repository
 */
public class ReservationRepository extends BaseRepository<Reservation> {

    private static final String FILE_PATH = "data/reservations.csv";

    /**
     * 생성자
     */
    public ReservationRepository() {
        super(FILE_PATH);
    }

    /**
     * 파일에서 예약 데이터 로드
     * *  CSV 파일 저장 형식 반영:
     * reservationId,studentId,spaceId,personCount,startTime,endTime
     */
    @Override
    protected void loadFromFile() {

        for (String line : readLines()) {

            String[] parts = line.split(",");

            // 데이터 개수가 정확히 6개인지 검증
            if (parts.length == 6) {

                // 1. 순서대로 데이터 파싱 (팀원의 CSV 포맷 기준)
                String reservationId = parts[0].trim();
                String studentId     = parts[1].trim();
                String spaceId       = parts[2].trim();
                
                // 인원수는 int 타입 변환
                int personCount      = Integer.parseInt(parts[3].trim());

                // 날짜 시간 파싱
                LocalDateTime startTime = LocalDateTime.parse(parts[4].trim());
                LocalDateTime endTime   = LocalDateTime.parse(parts[5].trim());

                // 
                dataList.add(
                        new Reservation(
                                reservationId,
                                studentId,
                                spaceId,
                                personCount,
                                startTime,
                                endTime
                        )
                );
            }
        }
    }

    /**
     * Reservation 객체를 CSV 문자열로 변환
     */
    @Override
    protected String toCsvLine(Reservation reservation) {
        return reservation.toCsvLine();
    }

    /**
     * 예약 추가
     *
     * 같은 공간의 예약 시간이 겹치면 false 반환
     * synchronized로 동시 예약 충돌 방지
     */
    public synchronized boolean addReservation(Reservation reservation) {

        for (Reservation existing : dataList) {

            if (existing.getSpaceId().equals(reservation.getSpaceId())) {

                boolean overlap =
                        reservation.getStartTime().isBefore(existing.getEndTime())
                        &&
                        reservation.getEndTime().isAfter(existing.getStartTime());

                if (overlap) {
                    return false;
                }
            }
        }

        save(reservation);
        return true;
    }

    /**
     * 특정 사용자의 예약 조회
     */
    public synchronized List<Reservation> findByStudentId(String studentId) {

        return findBy(
                reservation -> reservation.getStudentId().equals(studentId)
        );
    }

    /**
     * 예약 ID로 조회
     * *
     */
    public synchronized Reservation findByReservationId(String reservationId) {

        List<Reservation> result = findBy(
                r -> r.getReservationId().equals(reservationId)
        );

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }
}
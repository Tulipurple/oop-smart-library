package controller;

import model.Reservation;
import model.User;
import repository.ReservationRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 로그인 및 예약 기능을 처리하는 컨트롤러
 */
public class ReservationController {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 생성자
     */
    public ReservationController() {
        this.userRepository = new UserRepository();
        this.reservationRepository = new ReservationRepository();
    }

    /**
     * 로그인 또는 신규 사용자 등록
     */
    public synchronized User loginOrRegister(
            String studentId,
            String name
    ) {

        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        User user =
                userRepository.findByStudentId(
                        studentId.trim()
                );

        if (user != null) {

            if (user.getName().equals(name.trim())) {
                return user;
            }

            return null;
        }

        User newUser =
                new User(
                        studentId.trim(),
                        name.trim()
                );

        userRepository.save(newUser);

        return newUser;
    }

    /**
     * 예약 생성
     *
     * 예약 성공 : true
     * 예약 중복 : false
     */
    public synchronized boolean makeReservation(
            String studentId,
            String spaceId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {

        long reservationId =
                System.currentTimeMillis();

        Reservation reservation =
                new Reservation(
                        reservationId,
                        startTime,
                        endTime,
                        studentId,
                        spaceId,
                        "사용중"
                );

        return reservationRepository
                .addReservation(reservation);
    }

    /**
     * 특정 사용자의 예약 조회
     */
    public synchronized List<Reservation> getReservationsByStudent(
            String studentId
    ) {

        return reservationRepository
                .findByStudentId(studentId);
    }

    /**
     * 예약 ID 조회
     */
    public synchronized Reservation getReservationById(
            long reservationId
    ) {

        return reservationRepository
                .findByReservationId(reservationId);
    }
}
package controller;

import model.User;
import repository.UserRepository;

/**
 * 사용자 인증, 기존 정보 확인 및 
 * 자동 회원가입 비즈니스 로직을 처리하는 전용 컨트롤러
 */
public class UserController {

    private final UserRepository userRepository;

    /**
     * 생성자
     */
    public UserController() {
       // UserRepository 객체 생성
        this.userRepository = new UserRepository();
    }

    /**
     * 로그인 또는 신규 사용자 등록 (회원가입)
     * * @param studentId 입력받은 학번
     * @param name 입력받은 이름
     * @return 인증 성공 또는 가입 완료 시 User 객체, 유효하지 않거나 인증 실패 시 null
     */
    public synchronized User loginOrRegister(String studentId, String name) {

        // 1. 기본적인 입력값 유효성 검증 (Validation)
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String trimmedStudentId = studentId.trim();
        String trimmedName = name.trim();

        // 2. UserRepository를 통해 기존에 존재하는 학번인지 확인
        User existingUser = userRepository.findByStudentId(trimmedStudentId);

        // 3. 기존 사용자 흐름 처리 (로그인 검증)
        if (existingUser != null) {
            // 저장된 이름과 입력된 이름이 정확히 일치하는지 확인
            if (existingUser.getName().equals(trimmedName)) {
                return existingUser; // 로그인 성공
            }
            return null; // 학번은 존재하나 이름이 일치하지 않는 경우 (보안 및 예외 처리)
        }

        // 4. 신규 사용자 흐름 처리 (자동 회원가입)
        User newUser = new User(trimmedStudentId, trimmedName);
        userRepository.save(newUser); // UserRepository 부모의 save 메서드를 통해 CSV 파일에 영속화

        return newUser; // 회원가입 및 자동 로그인 성공
    }
}
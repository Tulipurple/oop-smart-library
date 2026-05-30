package repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Repository의 공통 기능을 제공하는 제너릭 추상 클래스
 */
public abstract class BaseRepository<T> {

    protected final List<T> dataList;
    protected final String filePath;

    // 생성자: 파일 경로를 받아 프로그램 시작 시 데이터를 로드
    public BaseRepository(String filePath) {
        this.filePath = filePath;
        this.dataList = new ArrayList<>();
        loadFromFile(); 
    }

    protected abstract void loadFromFile();
    protected abstract String toCsvLine(T item);

    // ───────────────────────────────────────────────────────
    // 공통 CRUD 메서드
    // ───────────────────────────────────────────────────────
    public synchronized List<T> findAll() {
        return new ArrayList<>(dataList);
    }

    public synchronized List<T> findBy(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : dataList) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public synchronized void save(T item) {
        dataList.add(item);
        saveToFile();
    }

    // ───────────────────────────────────────────────────────
    // 공통 파일 I/O 메서드
    // ───────────────────────────────────────────────────────
    protected synchronized void saveToFile() {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (T item : dataList) {
                writer.write(toCsvLine(item));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    protected List<String> readLines(){
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return lines; // 파일 없으면 빈 리스트 반환
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}

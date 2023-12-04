package com.vision.haksa.compkeys;
import java.io.Serializable;
import java.util.Objects;

public class EnrollmentId implements Serializable {
    private String coursecode;
    private String ssn;

    // 기본 생성자
    public EnrollmentId() {
    }

    public EnrollmentId(String coursecode, String ssn) {
        this.coursecode = coursecode;
        this.ssn = ssn;
    }

    // Getter 및 Setter 메소드

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentId that = (EnrollmentId) o;
        return Objects.equals(coursecode, that.coursecode) &&
               Objects.equals(ssn, that.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coursecode, ssn);
    }
}
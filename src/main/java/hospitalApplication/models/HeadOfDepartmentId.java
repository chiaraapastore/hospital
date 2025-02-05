package hospitalApplication.models;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class HeadOfDepartmentId implements Serializable {

    private String departmentId;
    private String headOfDepartmentId;

    public HeadOfDepartmentId() {}

    public HeadOfDepartmentId(String departmentId, String headOfDepartmentId) {
        this.departmentId = departmentId;
        this.headOfDepartmentId = headOfDepartmentId;
    }

    public HeadOfDepartmentId(Long departmentId, Long capoRepartoId) {
        this.departmentId = String.valueOf(departmentId);
        this.headOfDepartmentId = String.valueOf(capoRepartoId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeadOfDepartmentId that = (HeadOfDepartmentId) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(headOfDepartmentId, that.headOfDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, headOfDepartmentId);
    }
}


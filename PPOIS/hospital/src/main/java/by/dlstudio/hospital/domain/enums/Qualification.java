package by.dlstudio.hospital.domain.enums;

import java.util.Set;

public enum Qualification {
    TEST(Set.of(DiseaseType.TEST_ONE,DiseaseType.TEST_TWO));

    private final Set<DiseaseType> curableDiseases;

    Qualification(Set<DiseaseType> curableDiseases) {
        this.curableDiseases = curableDiseases;
    }

    public Set<DiseaseType> getCurableDiseases() {
        return curableDiseases;
    }
}

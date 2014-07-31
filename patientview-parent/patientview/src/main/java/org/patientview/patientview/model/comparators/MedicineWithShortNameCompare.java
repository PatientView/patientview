package org.patientview.patientview.model.comparators;

import org.patientview.patientview.medicine.MedicineWithShortName;
import java.util.Comparator;

public class MedicineWithShortNameCompare implements Comparator<MedicineWithShortName> {

    public MedicineWithShortNameCompare() {

    }

    @Override
    public int compare(MedicineWithShortName medicine1, MedicineWithShortName medicine2) {
        // compare using dates, used in ordering with Collections.sort
        return medicine1.getStartdate().before(medicine2.getStartdate()) ? 1 : -1;
    }
}

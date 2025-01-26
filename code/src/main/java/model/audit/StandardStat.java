package model.audit;

import java.util.UUID;

public class StandardStat {
    private final String idStandardStat;
    private String status;
    private Solution solution;


    // Default Constructor
    public StandardStat() {
        this.idStandardStat = UUID.randomUUID().toString();
        this.status = "notYet";
        this.solution = new Solution();
    }


    // Parameterized Constructor


    public StandardStat(String idStandardStat, String status, Solution solution) {
        this.idStandardStat = idStandardStat;
        this.setStatus(status);
        this.solution = solution;
    }

    public String getStatus() {
        return status;
    }
    public Solution getSolution() {
        return solution;
    }
    // Getters and Setters
    public String getIdStandardStat() {
        return idStandardStat;
    }

    public void setStatus(String status) {

            this.status = status;

    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "StandardStat{" +
                "idStandard='" + idStandardStat +
                '}';
    }
}

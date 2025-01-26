package model.audit;

import java.util.UUID;

public class RequirementStat {
    private final String idRequirementStat;
    private String status;
    private Solution solution;

    // Default Constructor
    public RequirementStat() {
        this.idRequirementStat = UUID.randomUUID().toString();
        this.status = "notYet";
        this.solution = new Solution();
    }

    // Parameterized Constructor
    public RequirementStat(String status, FinalReport clauseRapport, Solution solution) {
        this.idRequirementStat = UUID.randomUUID().toString();
        this.setStatus(status);
        this.solution = solution;
    }

    // Getters and Setters
    public String getIdRequirementStat() {
        return idRequirementStat;
    }

    public String getStatus() {
        return status;

    }

    public void setStatus(String status) {

            this.status = status;
        }





    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "ExigenceStat{" +
                "idExigence='" + idRequirementStat + '\'' +
                ", passStat='" + status + '\'' +
                ", Solution=" + solution +
                '}';
    }
}

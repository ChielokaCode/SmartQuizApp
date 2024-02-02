package smartquizapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartquizapp.config.SeedProperties;
import smartquizapp.model.QuizImage;
import smartquizapp.model.Subject;
import smartquizapp.repository.QuizImageRepository;
import smartquizapp.repository.SubjectRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataLoader {

    private final SubjectRepository subjectRepository;
    private final QuizImageRepository quizImageRepository;
    private final SeedProperties seedProperties;
    @Autowired
    public DataLoader(SubjectRepository subjectRepository, QuizImageRepository quizImageRepository, SeedProperties seedProperties){
        this.subjectRepository = subjectRepository;
        this.quizImageRepository = quizImageRepository;
        this.seedProperties = seedProperties;
    }

    @PostConstruct
    public void loadData() {
        if (seedProperties.isEnabled()) {
            seedSubjects();
            seedQuizImages();
        }
    }

    private void seedSubjects() {
        if (subjectRepository.count() == 0) {
            Subject physics = new Subject("Physics",
                    "Physics is the scientific study of matter, energy, space and time. It seeks to understand the fundamental principles that govern the natural world, describing and prediction physical phenomena through Laws and Theories",
                    "/physics.png",
                    "rgba(255, 102, 0, 0.20)");
            Subject science = new Subject("Science",
                    "Science is the systematic exploration and understanding of natural world through observations, experimentation's and analysis. It encompasses a broad range of disciplines including Physics, Chemistry, Biology etc.",
                    "/science.png",
                    "rgba(255, 102, 178, 0.20)");
            Subject finance = new Subject("Finance",
                    "Finance is the discipline that involves the management of money, investments and financial resources. It encompasses a range of activities including budgeting, investing, banking and risk management.",
                    "/finance.png",
                    "rgba(0, 116, 204, 0.20)");
            Subject geography = new Subject("Geography",
                    "Geography is the study of the Earth's landscapes, environments and the relationships between people and their surroundings.",
                    "/geography.png",
                    "rgba(39, 174, 96, 0.20)");
            Subject chemistry = new Subject("Chemistry",
                    "Chemistry is the scientific discipline that explores the properties, composition and behaviour of matter. It delves into the structures of atoms and molecules, interactions between substance and the changes in their states.",
                    "/chemistry.png",
                    "rgba(255, 0, 0, 0.20)");
            Subject languages = new Subject("Languages",
                    "Languages are intricate system of communication that enables individuals to express thoughts, emotions and ideas.These diverse and dynamic systems comes in various forms, encompassing spoken, written and symbols.",
                    "/language.png",
                    "var(--Indigo-200, #C7D7FE)");

            subjectRepository.saveAll(List.of(physics, science, finance, geography, chemistry, languages));
        }
    }
    private void seedQuizImages(){
        if (quizImageRepository.count() == 0){
            QuizImage undraw_finance = new QuizImage("/undraw_finance_re_gnv2.png");
            QuizImage undraw_investing = new QuizImage("/undraw_investing_re_bov7.png");
            QuizImage undraw_investment_data = new QuizImage("/undraw_investment_data_re_sh9x.png");
            QuizImage undraw_credit_card_payments = new QuizImage("/undraw_credit_card_payments_re_qboh.png");
            QuizImage undraw_invest = new QuizImage("/undraw_invest_re_8jl5.png");
            QuizImage undraw_payments = new QuizImage("/undraw_payments_re_77x0.png");
            QuizImage undraw_creative_experiment = new QuizImage("/static/undraw_creative_experiment_-8-dk3.png");
            QuizImage undraw_savings = new QuizImage("/undraw_savings_re_eq4w.png");
            QuizImage undraw_vault = new QuizImage("/undraw_vault_re_s4my.png");

            quizImageRepository.saveAll(List.of(undraw_finance, undraw_investing, undraw_investment_data, undraw_credit_card_payments,
                    undraw_invest, undraw_payments, undraw_creative_experiment, undraw_savings, undraw_vault));
        }

    }
}
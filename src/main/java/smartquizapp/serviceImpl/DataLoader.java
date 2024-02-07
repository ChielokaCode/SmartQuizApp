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
            QuizImage biology = new QuizImage("/biology_medicalcare.svg");
            QuizImage biology1 = new QuizImage("/biology_medicine.svg");
            QuizImage finance = new QuizImage("/business_chart.svg");
            QuizImage chemistry = new QuizImage("/chemical_science.svg");
            QuizImage language = new QuizImage("/communication.svg");
            QuizImage geography = new QuizImage("/environmental_geography.svg");
            QuizImage finance1 = new QuizImage("/financial_accounting.svg");
            QuizImage language1 = new QuizImage("/global_languages.svg");
            QuizImage geography1 = new QuizImage("/physical_geography.svg");
            QuizImage physics = new QuizImage("/physics_lab.svg");
            QuizImage science = new QuizImage("/physiology_science.svg");
            QuizImage science1 = new QuizImage("/world_map.svg");

            quizImageRepository.saveAll(List.of(biology, biology1, finance, chemistry,
                    language, geography, finance1, language1, geography1, physics, science, science1));
        }

    }
}
package smartquizapp.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import smartquizapp.dto.*;
import smartquizapp.enums.Role;
import smartquizapp.exception.*;
import smartquizapp.model.*;
import smartquizapp.repository.*;
import smartquizapp.service.QuizService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService {
    private final QuizTestRepository quizTestRepository;
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final StudentResponseRepository studentResponseRepository;
    private final EmailServiceImpl emailService;
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    public QuizServiceImpl(QuizTestRepository quizTestRepository, QuestionRepository questionRepository, SubjectRepository subjectRepository, StudentResponseRepository studentResponseRepository, EmailServiceImpl emailService, UserRepository userRepository) {
        this.quizTestRepository = quizTestRepository;
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.studentResponseRepository = studentResponseRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }



    @Override
    public void createQuizQuestion(QuizTestDto quizTestDto, Boolean isPublish) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);

        if(user==null){
            throw new UserNotVerifiedException("User not found");
        }
        if (!user.getUserRole().equals(Role.EDUCATOR)){
            throw new UserNotVerifiedException("You are not allowed to create quiz");
        }

        if (isPublish) {
            Quiz quiz = new Quiz();
            quiz.setTopic(quizTestDto.getTopic());
            quiz.setTimeLimit(quizTestDto.getTimeLimit());
            quiz.setUser(user);
            quiz.setIsPublish(true);
            quiz.setImageUrl(quizTestDto.getImageUrl());
            Subject subject = subjectRepository.findByNameIgnoreCase(quizTestDto.getSubjectType());
            quiz.setSubjectType(subject);
            quiz.setIsPrivate(false);
            quiz.setTotalMarks(quizTestDto.getTotalMarks());
            quiz.setDescription(quizTestDto.getDescription());
            Quiz saveQuiz = quizTestRepository.save(quiz);
            logger.info(""+saveQuiz);

            List<Question> questionList = quizTestDto.getQuestions();
            if (questionList != null) {
                for (Question question : questionList) {
                    Question saveQuestion = new Question();
                    saveQuestion.setUser(user);
                    saveQuestion.setQuiz(saveQuiz);
                    saveQuestion.setQuestionType(question.getQuestionType());
                    saveQuestion.setQuestionContent(question.getQuestionContent());
                    saveQuestion.setPoint(question.getPoint());
                    saveQuestion.setOptions(question.getOptions());
                    saveQuestion.setAnswer(question.getAnswer());
                    saveQuestion.setExplanation(question.getExplanation());
                    questionRepository.save(saveQuestion);
                }
            }



        }
        if(!isPublish){
            Quiz quiz = new Quiz();
            quiz.setTopic(quizTestDto.getTopic());
            quiz.setTimeLimit(quizTestDto.getTimeLimit());
            quiz.setUser(user);
            quiz.setIsPublish(false);
            quiz.setImageUrl(quizTestDto.getImageUrl());
            Subject subject = subjectRepository.findByNameIgnoreCase(quizTestDto.getSubjectType());
            quiz.setSubjectType(subject);
            quiz.setIsPrivate(false);
            quiz.setTotalMarks(quizTestDto.getTotalMarks());
            quiz.setDescription(quizTestDto.getDescription());
            Quiz saveQuiz = quizTestRepository.save(quiz);
            logger.info(""+saveQuiz);

            List<Question> questionList = quizTestDto.getQuestions();
            if (questionList != null) {
                for (Question question : questionList) {
                    Question saveQuestion = new Question();
                    saveQuestion.setUser(user);
                    saveQuestion.setQuiz(saveQuiz);
                    saveQuestion.setQuestionType(question.getQuestionType());
                    saveQuestion.setQuestionContent(question.getQuestionContent());
                    saveQuestion.setPoint(question.getPoint());
                    saveQuestion.setOptions(question.getOptions());
                    saveQuestion.setAnswer(question.getAnswer());
                    saveQuestion.setExplanation(question.getExplanation());
                    questionRepository.save(saveQuestion);
                }
            }
        }
    }

    @Override
    public String deleteQuiz(Long quizId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Quiz quiz = quizTestRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException("Quiz cannot be found!"));
        if(user.getUserRole()==Role.EDUCATOR && Objects.equals(user.getId(), quiz.getUser().getId())) {
            questionRepository.deleteByQuiz(quiz);
            quizTestRepository.delete(quiz);
            return "Quiz deleted successfully";
        }
        throw new UserNotVerifiedException("YOU DO NOT HAVE ACCESS TO DELETE QUIZ");
    }

    @Override
    public QuizResponseDto getQuizbyEducator(Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);
        Quiz quiz = quizTestRepository.findByIdAndUser(quizId, user);

        if (quiz != null && user.getUserRole() == Role.EDUCATOR && Objects.equals(user.getId(), quiz.getUser().getId())) {
            List<Question> question = questionRepository.findAllByQuizId(quiz.getId());
            return convertToQuizResponseDTO(quiz, question);
        }else {
            throw new UserNotVerifiedException("Unauthorized access to quizId: " + quizId);
        }
    }


    private QuizResponseDto convertToQuizResponseDTO(Quiz quiz, List<Question> question) {
        QuizResponseDto quizResponseDTO = new QuizResponseDto();
        quizResponseDTO.setId(quiz.getId());
        quizResponseDTO.setTopic(quiz.getTopic());
        quizResponseDTO.setDescription(quiz.getDescription());
        quizResponseDTO.setImageUrl(quiz.getImageUrl());
        quizResponseDTO.setSubjectType(quiz.getSubjectType());
        quizResponseDTO.setTotalMarks(quiz.getTotalMarks());
        quizResponseDTO.setTimeLimit(quiz.getTimeLimit());
        quizResponseDTO.setUserFirstName(quiz.getUser().getFirstName());
        quizResponseDTO.setUserLastName(quiz.getUser().getLastName());
        List<Question> questionList1 = questionRepository.findAllByQuiz(quiz);
        quizResponseDTO.setQuestionCount(questionList1.size());

        List<QuestionDto> questionList = new ArrayList<>();
        for(Question question1 : question){
            QuestionDto question2 = new QuestionDto();
            question2.setQuestionContent(question1.getQuestionContent());
            question2.setOptions(question1.getOptions());
            question2.setAnswer(question1.getAnswer());
            question2.setQuestionType(question1.getQuestionType());
            question2.setPoint(question1.getPoint());
            question2.setExplanation(question1.getExplanation());
            questionList.add(question2);
        }
        quizResponseDTO.setQuestions(questionList);
        return quizResponseDTO;
    }

    @Override
    public void editQuizById(Long id, QuizTestDto quizTestDto) {
        Quiz quiz = quizTestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findUserByEmail(username);
        if (!Objects.equals(currentUser.getUsername(), quiz.getUser().getUsername())) {
            throw new UserNotVerifiedException("Unauthorized: You do not own this quiz");
        }

        if (quizTestDto.getSubjectType() != null && !quizTestDto.getSubjectType().isEmpty()) {
            Subject subject = subjectRepository.findByNameIgnoreCase(quizTestDto.getSubjectType());
            quiz.setSubjectType(subject);
        }
        if (quizTestDto.getDescription() != null && !quizTestDto.getDescription().isEmpty()) {
            quiz.setDescription(quizTestDto.getDescription());
        }
        if (!(quizTestDto.getTimeLimit() <= 0)) {
            quiz.setTimeLimit(quizTestDto.getTimeLimit());
        }
        if (quizTestDto.getTopic() != null && !quizTestDto.getTopic().isEmpty()) {
            quiz.setTopic(quizTestDto.getTopic());
        }
        if (quizTestDto.getImageUrl() != null && !quizTestDto.getImageUrl().isEmpty()) {
            quiz.setImageUrl(quizTestDto.getImageUrl());
        }
        if (quizTestDto.getTotalMarks() != null) {

            quiz.setTotalMarks(quizTestDto.getTotalMarks());
        }
        if (quizTestDto.getIsPublish() != null) {
            quiz.setIsPublish(quizTestDto.getIsPublish());
        }
        if (quizTestDto.getIsPrivate() != null){
            quiz.setIsPrivate(quizTestDto.getIsPrivate());
        }
        quizTestRepository.save(quiz);
    }
    @Override
    public void publishQuiz(Long quizId)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);
        Quiz quiz = quizTestRepository.findById(quizId).orElseThrow(()->new QuizNotFoundException("quiz not found"));
        if(user.getUserRole()==Role.EDUCATOR && Objects.equals(user.getId(), quiz.getUser().getId())) {
            quiz.setIsPublish(true);
            quizTestRepository.save(quiz);
        }else {
            throw new UserNotVerifiedException("You are not allowed to publish this quiz");
        }
    }

@Override
    public List<QuizResponseDto> getAllDraftPublishQuiz(Boolean isPublish) {
        User user1=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(isPublish!=null) {
        List<Quiz>quizList= quizTestRepository.findAllByIsPublishAndUser(isPublish, user1);
        List<QuizResponseDto>quizResponseDtos= new ArrayList<>();

            if (quizList == null) {
                throw new QuizNotFoundException("quiz type not found");
            }


            for (Quiz quiz : quizList) {

                List<Question> questions = questionRepository.findAllByQuiz(quiz);
                List<QuestionDto> questionDtoList = new ArrayList<>();

                for (Question question : questions) {
                    QuestionDto questionDto = new QuestionDto();
                    questionDto.setQuestionType(question.getQuestionType());
                    questionDto.setQuestionContent(question.getQuestionContent());
                    questionDto.setExplanation(question.getExplanation());
                    questionDto.setPoint(question.getPoint());
                    questionDto.setAnswer(question.getAnswer());
                    questionDto.setOptions(question.getOptions());
                    questionDtoList.add(questionDto);
                }


                QuizResponseDto quizResponseDto = new QuizResponseDto();
                quizResponseDto.setSubjectType(quiz.getSubjectType());
                quizResponseDto.setTopic(quiz.getTopic());
                quizResponseDto.setTotalMarks(quiz.getTotalMarks());
                quizResponseDto.setQuestions(questionDtoList);
                quizResponseDto.setIsPrivate(quiz.getIsPrivate());
                quizResponseDto.setDescription(quiz.getDescription());
                quizResponseDto.setImageUrl(quiz.getImageUrl());
                quizResponseDto.setTimeLimit(quiz.getTimeLimit());
                quizResponseDto.setTotalMarks(quiz.getTotalMarks());
                quizResponseDto.setIsPublish(quiz.getIsPublish());
                quizResponseDtos.add(quizResponseDto);
            }
            return quizResponseDtos;
        }

        List<Quiz>quizAll= quizTestRepository.findAll();
        List<QuizResponseDto>quizResponseDtoList= new ArrayList<>();

        if (quizAll == null) {
            throw new QuizNotFoundException("quiz type not found");
        }


        for (Quiz quiz : quizAll) {

            List<Question> questions = questionRepository.findAllByQuiz(quiz);
            List<QuestionDto> questionDtoList = new ArrayList<>();

            for (Question question : questions) {
                QuestionDto questionDto = new QuestionDto();
                questionDto.setQuestionType(question.getQuestionType());
                questionDto.setQuestionContent(question.getQuestionContent());
                questionDto.setExplanation(question.getExplanation());
                questionDto.setPoint(question.getPoint());
                questionDto.setAnswer(question.getAnswer());
                questionDto.setOptions(question.getOptions());
                questionDtoList.add(questionDto);
            }


            QuizResponseDto quizResponseDto = new QuizResponseDto();
            quizResponseDto.setSubjectType(quiz.getSubjectType());
            quizResponseDto.setTopic(quiz.getTopic());
            quizResponseDto.setTotalMarks(quiz.getTotalMarks());
            quizResponseDto.setQuestions(questionDtoList);
            quizResponseDto.setIsPrivate(quiz.getIsPrivate());
            quizResponseDto.setDescription(quiz.getDescription());
            quizResponseDto.setImageUrl(quiz.getImageUrl());
            quizResponseDto.setTimeLimit(quiz.getTimeLimit());
            quizResponseDto.setTotalMarks(quiz.getTotalMarks());
            quizResponseDto.setIsPublish(quiz.getIsPublish());
            quizResponseDtoList.add(quizResponseDto);
        }
        return quizResponseDtoList;
    }




    @Override
    public String sendInviteEmail(SendInviteEmailRequestDto invite,Long quizId) throws MailConnectionException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);

        // Check if user is authenticated
        if (user == null) {
            throw new UserNotVerifiedException("User is not authenticated");
        }

        // Fetch the quiz by ID
        Quiz quiz = quizTestRepository.findByUserIdAndId(user.getId(), quizId);

        // Check if quiz is found
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not found with ID: " + quizId);
        }

        // Check user role
        if (!user.getUserRole().equals(Role.EDUCATOR)){
            throw new UserNotVerifiedException("You don't have Permission to send invites");
        }

        String singleEmail = invite.getEmail();
        String[] splitEmail = singleEmail.split(",");

        for (String email : splitEmail) {
            String name = email.split("@")[0];
            String url = "http://localhost:5173" + "/start-quiz/" + quiz.getId();
try {
    emailService.sendSimpleEmail(
            email.trim(),
            "Dear " + name + ", " + "\n" +
                    "\n" +
                    "We hope this email finds you well. Your educator has created an exciting quiz on the Smart Quiz app, and we invite you to participate and showcase your knowledge!\n" +
                    "\n" +
                    "Quiz Name: " + quiz.getTopic() + "\n" +
                    "Educator: " + user.getFirstName() + "\n" +
                    "Subject: " + quiz.getSubjectType().getName() + "\n" +
                    "Total Time: " + quiz.getTimeLimit() + " minutes" + "\n" +
                    "\n" +
                    "How to Participate:\n" +
                    "1. Click on the following link to access the Smart Quiz app: " + url + "\n" +
                    "2. Log in using your student credentials.\n" +
                    "3. Find the quiz under the \"Active Quizzes\" section on your dashboard.\n" +
                    "4. Click \"Start Quiz\" to begin the exciting learning experience.\n" +
                    "\n" +
                    "Important Notes:\n" +
                    "- Ensure a stable internet connection for a seamless quiz experience.\n" +
                    "- Complete the quiz within the specified timeframe to qualify for assessment.\n" +
                    "- If you encounter any technical issues, please reach out to our support team at [Support Email/Phone].\n" +
                    "\n" +
                    "We encourage you to embrace this opportunity to enhance your understanding of the subject matter while having fun. Good luck, and may your knowledge shine bright!\n" +
                    "\n" +
                    "Best Regards,\n" +
                    "\n" +
                    user.getFirstName() + "\n" +
                    "Smart Quiz Team\n" +
                    "\n" +
                    "P.S. Stay tuned for more engaging quizzes and learning experiences on the Smart Quiz app!",
            "Join the Smart Quiz - Engage in a Fun Learning Experience!"
    );
}catch (Exception e){
    throw new MailConnectionException("Error sending Mail");
}
        }
        return "Invite Email Sent Successfully";
    }

    @Override
    public List<QuizResponseDto> getQuizzesBySubject(String subjectName) {

        Subject subject = subjectRepository.findByNameIgnoreCase(subjectName);

        if (subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }

        List<Quiz> quizzes = quizTestRepository.findAllBySubjectType(subject);
        List<QuizResponseDto> quizResponseDTOs = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            List<Question> questions = questionRepository.findAllByQuizId(quiz.getId());
            quizResponseDTOs.add(convertToQuizResponseDTO(quiz, questions));
        }

        return quizResponseDTOs;
    }


    @Override
    public QuizResponseDto getQuiz(Long quizId) {
        Quiz quiz = quizTestRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        validateQuizAvailability(quiz);

        List<Question> questions = questionRepository.findAllByQuizId(quizId);
        List<Question> mutableQuestions = new ArrayList<>(questions);
        Collections.shuffle(mutableQuestions);

        return convertToQuizResponseDTO(quiz, questions);
    }

    @Override
    public void submitQuiz(Long quizId, StudentResponseDto studentResponse) {
        Quiz quiz = quizTestRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        if (!quiz.getIsPublish()) {
            throw new StudentResponseBadRequest("Quiz is not published yet");
        }

        if (quiz.getTimeLimit() == 0 && !isWithinTimeWindow(quiz)) {
            throw new StudentResponseBadRequest("Quiz has ended");
        }

        List<Question> questions = questionRepository.findAllByQuizId(quiz.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);

        StudentResponse response = new StudentResponse();

        for (Question question : questions) {
            String answer = question.getAnswer();
            response.setScores(studentResponse.getScores());
            response.setQuestion(question);
            response.setStudent(user);
            response.setId(studentResponse.getId());
            response.setAnswer(answer);

        }
        studentResponseRepository.save(response);
    }

    private void validateQuizAvailability(Quiz quiz) {

    }

    private void validateAndSaveStudentResponses(Quiz quiz, List<StudentResponseDto> responses) {
        List<Question> questions = questionRepository.findAllByQuizId(quiz.getId());

        if (responses.size() > questions.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many student responses");
        }

        User student = getCurrentUser();

        List<StudentResponse> capturedResponses = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            if (i < responses.size()) {
                StudentResponseDto studentResponseDto = responses.get(i);

                StudentResponse capturedResponse = new StudentResponse();
                capturedResponse.setQuestion(question);
                capturedResponse.setStudent(student);
                capturedResponse.setScores(studentResponseDto.getScores());

                capturedResponses.add(capturedResponse);
            }
        }

        studentResponseRepository.saveAll(capturedResponses);
    }

    public void submit(Quiz quiz, StudentResponseDto studentResponse){
        List<Question> questions = questionRepository.findAllByQuizId(quiz.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);

        StudentResponse response = new StudentResponse();

        for (Question question : questions) {
            String answer = question.getAnswer();
            response.setScores(studentResponse.getScores());
            response.setQuestion(question);
            response.setStudent(user);
            response.setId(studentResponse.getId());
            response.setAnswer(answer);

        }
        studentResponseRepository.save(response);
    }

    private boolean isWithinTimeWindow(Quiz quiz) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime quizStartTime = getQuizStartTime(quiz);
        LocalDateTime quizEndTime = quizStartTime.plusMinutes(quiz.getTimeLimit());

        return currentTime.isBefore(quizEndTime);
    }

    private LocalDateTime getQuizStartTime(Quiz quiz) {
        // Implement logic to get the quiz start time from the database or elsewhere
        return LocalDateTime.now();
    }

    private User getCurrentUser() {
        // Implement logic to get the current user from the security context or authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        return user;
    }


}


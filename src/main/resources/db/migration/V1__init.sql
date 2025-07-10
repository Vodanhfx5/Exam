CREATE TABLE exam (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration_minutes INTEGER,
    location VARCHAR(255),
    timeslot TIMESTAMP,
    lesson_id BIGINT
);

CREATE TABLE question (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    text VARCHAR(1000) NOT NULL,
    correct_answer_index INTEGER NOT NULL
);

CREATE TABLE question_choices (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    choice TEXT NOT NULL
);

CREATE TABLE exam_result (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL,
    score INTEGER NOT NULL,
    submitted_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (exam_id, user_id)
);

CREATE TABLE exam_result_answers (
    exam_result_id BIGINT NOT NULL REFERENCES exam_result(id) ON DELETE CASCADE,
    answer INTEGER NOT NULL
);
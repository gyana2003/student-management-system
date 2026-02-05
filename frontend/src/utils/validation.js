// Validation utility functions

export const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const validateName = (name) => {
  return name.trim().length >= 2 && name.trim().length <= 100;
};

export const validateCourse = (course) => {
  return course.trim().length >= 2 && course.trim().length <= 100;
};

export const validateStudent = (student) => {
  const errors = {};

  if (!student.name || student.name.trim() === "") {
    errors.name = "Name is required";
  } else if (!validateName(student.name)) {
    errors.name = "Name must be between 2 and 100 characters";
  }

  if (!student.email || student.email.trim() === "") {
    errors.email = "Email is required";
  } else if (!validateEmail(student.email)) {
    errors.email = "Please enter a valid email";
  }

  if (!student.course || student.course.trim() === "") {
    errors.course = "Course is required";
  } else if (!validateCourse(student.course)) {
    errors.course = "Course must be between 2 and 100 characters";
  }

  return errors;
};

export const hasErrors = (errors) => {
  return Object.keys(errors).length > 0;
};

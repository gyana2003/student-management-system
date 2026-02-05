import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import authService from '../utils/authService';
import './ForgotPassword.css';

function ForgotPassword() {
  const [step, setStep] = useState(1); // Step 1: Enter email, Step 2: Enter reset code, Step 3: New password
  const [email, setEmail] = useState('');
  const [resetCode, setResetCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleRequestReset = async (e) => {
    e.preventDefault();
    setErrors({});

    if (!email) {
      setErrors({ email: 'Email is required' });
      return;
    }

    setLoading(true);
    try {
      await authService.requestPasswordReset(email);
      toast.success('Reset instructions sent to your email!');
      setStep(2);
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Failed to send reset email';
      setErrors({ form: errorMsg });
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyCode = async (e) => {
    e.preventDefault();
    setErrors({});

    if (!resetCode) {
      setErrors({ resetCode: 'Reset code is required' });
      return;
    }

    setLoading(true);
    try {
      const result = await authService.verifyResetCode(email, resetCode);
      if (result) {
        toast.success('Code verified!');
        setStep(3);
      }
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Invalid reset code';
      setErrors({ form: errorMsg });
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setErrors({});

    const validationErrors = {};
    if (!newPassword) validationErrors.newPassword = 'New password is required';
    if (newPassword.length < 6) validationErrors.newPassword = 'Password must be at least 6 characters';
    if (newPassword !== confirmPassword) validationErrors.confirmPassword = 'Passwords do not match';

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setLoading(true);
    try {
      await authService.resetPassword(email, resetCode, newPassword);
      toast.success('Password reset successful! Please login.');
      navigate('/login');
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Failed to reset password';
      setErrors({ form: errorMsg });
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="forgot-password-container">
      <div className="forgot-password-box">
        <div className="forgot-password-header">
          <h1>🔐 Reset Password</h1>
          <p>Step {step} of 3</p>
        </div>

        {/* Step 1: Enter Email */}
        {step === 1 && (
          <form onSubmit={handleRequestReset} className="forgot-password-form">
            {errors.form && <div className="error-message">{errors.form}</div>}

            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                type="email"
                id="email"
                placeholder="Enter your registered email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              {errors.email && <span className="error">{errors.email}</span>}
            </div>

            <button type="submit" disabled={loading} className="reset-button">
              {loading ? 'Sending...' : 'Send Reset Code'}
            </button>
          </form>
        )}

        {/* Step 2: Enter Reset Code */}
        {step === 2 && (
          <form onSubmit={handleVerifyCode} className="forgot-password-form">
            {errors.form && <div className="error-message">{errors.form}</div>}

            <p className="step-info">A reset code has been sent to {email}</p>

            <div className="form-group">
              <label htmlFor="resetCode">Reset Code</label>
              <input
                type="text"
                id="resetCode"
                placeholder="Enter the code from your email"
                value={resetCode}
                onChange={(e) => setResetCode(e.target.value.toUpperCase())}
                maxLength="6"
                required
              />
              {errors.resetCode && <span className="error">{errors.resetCode}</span>}
            </div>

            <button type="submit" disabled={loading} className="reset-button">
              {loading ? 'Verifying...' : 'Verify Code'}
            </button>

            <button 
              type="button" 
              onClick={() => setStep(1)} 
              className="back-button"
            >
              Back
            </button>
          </form>
        )}

        {/* Step 3: Enter New Password */}
        {step === 3 && (
          <form onSubmit={handleResetPassword} className="forgot-password-form">
            {errors.form && <div className="error-message">{errors.form}</div>}

            <div className="form-group">
              <label htmlFor="newPassword">New Password</label>
              <input
                type="password"
                id="newPassword"
                placeholder="Enter new password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
              {errors.newPassword && <span className="error">{errors.newPassword}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                placeholder="Confirm new password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
              {errors.confirmPassword && <span className="error">{errors.confirmPassword}</span>}
            </div>

            <button type="submit" disabled={loading} className="reset-button">
              {loading ? 'Resetting...' : 'Reset Password'}
            </button>

            <button 
              type="button" 
              onClick={() => setStep(2)} 
              className="back-button"
            >
              Back
            </button>
          </form>
        )}

        <div className="forgot-password-footer">
          <p>Remember your password? <Link to="/login">Login here</Link></p>
        </div>
      </div>
    </div>
  );
}

export default ForgotPassword;

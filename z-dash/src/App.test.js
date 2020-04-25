import React from 'react';
import { render } from '@testing-library/react';
import LoginPage from './route/page/anonymous/LoginPage';

test('renders the login page', () => {
  const { container } = render(<LoginPage />);
  expect(container.childElementCount).toBe(2);
});

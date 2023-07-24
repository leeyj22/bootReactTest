import React from "react";
import PropTypes from "prop-types";
import Link from "next/link";

const AppLayout = ({ children }) => {
  return (
    <div>
      <ul>
        <li>
          <Link href="/page2" target="_blank" rel="noopener noreferrer">
            page2
          </Link>
        </li>
      </ul>
      {children}
    </div>
  );
};

AppLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AppLayout;

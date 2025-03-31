import React from 'react';
import { Link } from 'react-router-dom';
import styles from './Header.module.css';

function Header() {
  return (
    <header className={styles.header}>
      <nav>
        <ul className={styles.navList}>

        <li className={styles.navItem}>
            <Link to="/login" className={`${styles.link} ${styles.sairHeader}`}>
              Sair
            </Link>
          </li>
          <li className={styles.navItem}>
            <Link to="/relatorio-fluxo-carros" className={`${styles.link} ${styles.otherHeaders}`}>
              Relatório de Fluxo de Carros
            </Link>
          </li>
          <li className={styles.navItem}>
            <Link to="/controle-mecanicos" className={`${styles.link} ${styles.otherHeaders}`}>
              Controle de Pagamentos Mecânicos
            </Link>
          </li>
          
          <li className={styles.navItem}>
            <Link to="/dashboard" className={`${styles.link} ${styles.otherHeaders}`}>
              Negócios
            </Link>
          </li>
          

        </ul>
      </nav>
    </header>
  );
}

export default Header;
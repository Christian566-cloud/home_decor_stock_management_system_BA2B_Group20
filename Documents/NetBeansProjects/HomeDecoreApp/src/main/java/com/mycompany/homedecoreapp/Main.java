/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.homedecoreapp;

import com.mycompany.homedecoreapp.UserAuths.LoginForm;
import javax.swing.SwingUtilities;


/**
 *
 * @author Kapnang
 */


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}

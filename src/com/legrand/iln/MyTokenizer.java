package com.legrand.iln;

import java.util.*;

/* $Id: MyTokenizer.java,v 1.4 2003/06/02 20:42:25 legrand Exp legrand $ */

/**
 * Progetto ILN
 * Copyright (C) 2003 Monsieur Legrand
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the license, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 */

/**
 * Tokenizer (piu' evoluto di StringTokenizer) per la tokenizzazione
 * in base a sequenze di caratteri.<br>
 *
 * @author Monsieur Legrand
 * @version 1.4
 * @date 02 giugno 2003
 */
public class MyTokenizer {
  Vector tokens = new Vector();
  int index;
  int maxTok;   //alla fine dell'esecuzione del costruttore conterra' il num di token presenti

  /**
   * Costruttore. Istanzia un nuovo oggetto di classe MyTokenizer.<br>
   * @param inp   La stringa da tokenizzare.
   * @param delim La stringa di caratteri che fa da delimitatore.
   */
  public MyTokenizer (String inp, String delim){
  	int k;
	String mInput = inp;
	index = 0;
	maxTok = 0;
        
	while ((k = mInput.indexOf(delim)) > 0){
  	     tokens.add(mInput.substring(0, k));
	     mInput = mInput.substring(k + delim.length());
	     maxTok++;
	}
	tokens.add(mInput);
	maxTok++;
  }

  /**
   * Restituisce il prossimo token,
   *
   * @return Una stringa contenente il token successivo
   */
  public String nextToken() {
  	String risposta = (String)tokens.get(index);
  	index++;
  	return risposta;
  }


  public boolean hasMoreToken(){
	  boolean response = false;
	  if (index<=maxTok)
		  response = true;
	  return response;
  }
}

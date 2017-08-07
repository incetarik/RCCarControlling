package com.incetarik.rccarcontrolling;

/**
 * Project: RCCarControlling
 * <p>
 * Date: 28 Dec 2016
 * Author: Tarık İNCE <incetarik@hotmail.com>
 */

/**
 * Interface for the shapes of this project.
 * Each shape should implement one (but important)
 */
public interface IShape {
    /**
     * Method explains what this object (really) is.
     * @param self The object itself. Mostly ignored, since object calls this
     * @return Name as String
     */
    String thisIsA(IShape self);
}
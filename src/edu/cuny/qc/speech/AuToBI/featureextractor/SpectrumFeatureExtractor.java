/*  SpectrumFeatureExtractor.java

    Copyright (c) 2009-2011 Andrew Rosenberg

    This file is part of the AuToBI prosodic analysis package.

    AuToBI is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AuToBI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AuToBI.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cuny.qc.speech.AuToBI.featureextractor;

import edu.cuny.qc.speech.AuToBI.SpectrumExtractor;
import edu.cuny.qc.speech.AuToBI.core.*;

import java.util.List;

/**
 * SpectrumFeatureExtractor extracts a spectrum from a given WavData object and aligns the appropriate sections to the
 * supplied regions.
 */
public class SpectrumFeatureExtractor extends FeatureExtractor {
  private WavData wav_data;  // the audio information to analyze
  private String feature_name;  // the name of the feature to hold pitch information
  private double frame_size; // The spectrum frame duration
  private double hamming_window; // The size of the hamming window used in the spectrum analysis


  /**
   * Constructs a new SpectrumFeatureExtractor to process wav_data and store the resulting Spectrum objects on feature_name
   *
   * This uses a default frame size of 0.01s, and a hamming window of 0.02s.
   *
   * @param wav_data the wave data to analyse
   * @param feature_name the feature name
   */
  public SpectrumFeatureExtractor(WavData wav_data, String feature_name) {
    this(wav_data, feature_name, 0.01, 0.02);
  }

  public SpectrumFeatureExtractor(WavData wav_data, String feature_name, double frame_size, double hamming_window) {
    this.wav_data = wav_data;
    this.feature_name = feature_name;
    this.frame_size = frame_size;
    this.hamming_window = hamming_window;

    this.extracted_features.add(feature_name);
  }
  /**
   * Extracts the spectrum and aligns information to regions.
   *
   * @param regions The regions to extract features from.
   * @throws FeatureExtractorException if there is a problem.
   */
  @Override
  public void extractFeatures(List regions) throws FeatureExtractorException {
    SpectrumExtractor extractor = new SpectrumExtractor(wav_data);
    try {
      Spectrum spectrum = extractor.getSpectrum(frame_size, hamming_window);

      for (Region r : (List<Region>) regions) {
        Spectrum sub_spectrum = spectrum.getSlice(r.getStart(), r.getEnd());
        r.setAttribute(feature_name, sub_spectrum);
      }
    } catch (AuToBIException e) {
      throw new FeatureExtractorException(e.getMessage());
    }
  }
}
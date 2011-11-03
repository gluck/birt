/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.util;

import java.util.Collection;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.notify.Adapter;

/**
 * This class provides methods to set default value into chart elements.
 * 
 */

public class ChartDefaultValueUtil extends ChartElementUtil
{

	/**
	 * Check if current chart use auto series palette.
	 * 
	 * @param chart
	 * @return
	 */
	public static boolean isAutoSeriesPalette( Chart chart )
	{
		SeriesDefinition sd = ChartUtil.getCategorySeriesDefinition( chart );
		return ( sd.getSeriesPalette( ).getEntries( ).size( ) == 0 );
	}
	
	/**
	 * Removes all series palettes.
	 * 
	 * @param chart
	 */
	public static void removeSerlesPalettes( Chart chart )
	{
		ChartUtil.getCategorySeriesDefinition( chart )
				.getSeriesPalette( )
				.getEntries( )
				.clear( );
		for ( SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions( chart ) )
		{
			sd.getSeriesPalette( ).getEntries( ).clear( );
		}
	}

	/**
	 * Updates series palettes to default values.
	 * 
	 * @param chart
	 */
	public static void updateSeriesPalettes( Chart chart )
	{
		updateSeriesPalettes( chart, null );
	}
	
	/**
	 * Updates series palettes to default values.
	 * 
	 * @param chart
	 * @param adapters
	 */
	public static void updateSeriesPalettes( Chart chart,
			Collection<? extends Adapter> adapters )
	{
		// Set series palette for category series definition.
		ChartUtil.getCategorySeriesDefinition( chart )
				.setSeriesPalette( DefaultValueProvider.defSeriesDefinition( 0 )
						.getSeriesPalette( )
						.copyInstance( ) );
		if ( adapters != null )
		{
			ChartUtil.getCategorySeriesDefinition( chart )
					.getSeriesPalette( )
					.eAdapters( )
					.addAll( adapters );
		}
		// Set series palettes for value series definitions.
		if ( ChartUtil.hasMultipleYAxes( chart ) )
		{
			int axesNum = ChartUtil.getOrthogonalAxisNumber( chart );
			for ( int i = 0; i < axesNum; i++ )
			{
				int pos = i;
				SeriesDefinition[] seriesDefns = ChartUtil.getOrthogonalSeriesDefinitions( chart,
						i )
						.toArray( new SeriesDefinition[]{} );
				for ( int j = 0; j < seriesDefns.length; j++ )
				{
					pos += j;
					seriesDefns[j].setSeriesPalette( DefaultValueProvider.defSeriesDefinition( pos )
							.getSeriesPalette( )
							.copyInstance( ) );
					if ( adapters != null )
					{
						seriesDefns[j].getSeriesPalette( )
								.eAdapters( )
								.addAll( adapters );
					}
				}
			}
		}
		else
		{
			int i = 0;
			for ( SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions( chart ) )
			{
				sd.setSeriesPalette( DefaultValueProvider.defSeriesDefinition( i )
						.getSeriesPalette( )
						.copyInstance( ) );
				if ( adapters != null )
				{
					sd.getSeriesPalette( ).eAdapters( ).addAll( adapters );
				}
				i++;
			}
		}
	}
	
	/**
	 * Returns default values of specified series object.
	 * 
	 * @param runtimeSeries specified series object.
	 * @return
	 */
	public static Series getSeriesDefault( Series runtimeSeries )
	{
		if ( runtimeSeries instanceof BarSeries )
		{
			return DefaultValueProvider.defBarSeries( );
		}
		else if ( runtimeSeries instanceof BubbleSeries )
		{
			return DefaultValueProvider.defBubbleSeries( );
		}
		else if ( runtimeSeries instanceof ScatterSeries )
		{
			return DefaultValueProvider.defScatterSeries( );
		}
		else if ( runtimeSeries instanceof DifferenceSeries )
		{
			return DefaultValueProvider.defDifferenceSeries( );
		}
		else if ( runtimeSeries instanceof AreaSeries )
		{
			return DefaultValueProvider.defAreaSeries( );
		}
		else if ( runtimeSeries instanceof LineSeries )
		{
			return DefaultValueProvider.defLineSeries( );
		}
		else if( runtimeSeries instanceof GanttSeries )
		{
			return DefaultValueProvider.defGanttSeries( );
		}
		else if( runtimeSeries instanceof DialSeries )
		{
			return DefaultValueProvider.defDialSeries( );
		}
		else if( runtimeSeries instanceof PieSeries )
		{
			return DefaultValueProvider.defPieSeries( );
		}
		else if( runtimeSeries instanceof StockSeries )
		{
			return DefaultValueProvider.defStockSeries( );
		}
		
		return null;
	}
}

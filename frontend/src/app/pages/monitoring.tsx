"use client";

import React, { useEffect, useState } from 'react';
import { Line, Doughnut } from 'react-chartjs-2';
import { Chart, ArcElement, CategoryScale, LinearScale, PointElement, LineElement, Tooltip } from 'chart.js';
import axios from 'axios';

Chart.register(ArcElement, CategoryScale, LinearScale, PointElement, LineElement, Tooltip);

interface TradeInfo {
    decision: string;
    percentage: number;
    reason: string;
    krwBalance: string;
    btcBalance: string;
    btcAvgBuyPrice: string;
    btcKrwPrice: number;
    reflection: string;
    orderStatus: string;
    createdAt: string;
}

interface PerformanceData {
    performance: number;
    tradeHistory: TradeInfo[];
    decisionDistribution: Record<string, number>;
}

const Monitoring: React.FC = () => {
    const [performanceData, setPerformanceData] = useState<PerformanceData | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/coin/monitoring');
                const { result } = response.data;
                setPerformanceData(result);
            } catch (error) {
                console.error('Error fetching performance data:', error);
            }
        };
        fetchData();
    }, []);

    if (!performanceData) {
        return <div>Loading...</div>;
    }

    const { performance, tradeHistory, decisionDistribution } = performanceData;

    const decisionData = {
        labels: Object.keys(decisionDistribution),
        datasets: [
            {
                data: Object.values(decisionDistribution),
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            },
        ],
    };

    const tradeHistoryLabels = tradeHistory.map((_, index) => `Trade ${index + 1}`);
    const tradePerformanceData = {
        labels: tradeHistoryLabels,
        datasets: [
            {
                label: 'Performance (%)',
                data: tradeHistory.map((trade) => trade.percentage),
                fill: false,
                borderColor: 'rgba(75,192,192,1)',
                tension: 0.1,
            },
        ],
    };

    return (
        <div className="container mx-auto p-4 mb-16">
            <h1 className="text-2xl font-bold mb-14">Bitcoin Auto Trade Monitoring</h1>

            <div className="mb-20">
                <h2 className="text-xl font-semibold mb-2">Current Performance</h2>
                <p className="text-lg">{performance.toFixed(2)}%</p>
            </div>

            <div className="mb-20">
                <h2 className="text-xl font-semibold mb-2">Trade History Details</h2>
                <div className="overflow-x-auto overflow-y-auto max-h-96 mb-16">
                    <table className="table-fixed w-full">
                        <thead>
                        <tr>
                            <th className="px-4 py-2 w-20">Trade #</th>
                            <th className="px-4 py-2 w-24">Decision</th>
                            <th className="px-4 py-2 w-24">Percentage</th>
                            <th className="px-4 py-2 w-[2000px]">Reason</th>
                            <th className="px-4 py-2 w-32">Order Status</th>
                            <th className="px-4 py-2 w-36">KRW Balance</th>
                            <th className="px-4 py-2 w-36">BTC Balance</th>
                            <th className="px-4 py-2 w-48">BTC Avg Buy Price</th>
                            <th className="px-4 py-2 w-40">BTC KRW Price</th>
                            <th className="px-4 py-2 w-[3800px]">Reflection</th>
                            <th className="px-4 py-2 w-40">Created At</th>
                        </tr>
                        </thead>
                        <tbody>
                        {tradeHistory.map((trade, index) => (
                            <tr key={index}>
                                <td className="border px-4 py-2">{index + 1}</td>
                                <td className="border px-4 py-2">{trade.decision}</td>
                                <td className="border px-4 py-2">{trade.percentage.toFixed(2)}%</td>
                                <td className="border px-4 py-2">{trade.reason}</td>
                                <td className="border px-4 py-2">{trade.orderStatus}</td>
                                <td className="border px-4 py-2">{trade.krwBalance}</td>
                                <td className="border px-4 py-2">{trade.btcBalance}</td>
                                <td className="border px-4 py-2">{trade.btcAvgBuyPrice}</td>
                                <td className="border px-4 py-2">{trade.btcKrwPrice}</td>
                                <td className="border px-4 py-2">{trade.reflection}</td>
                                <td className="border px-4 py-2">{trade.createdAt}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="mb-20">
                <h2 className="text-xl font-semibold mb-2">Trade Decision Distribution</h2>
                <div className="w-1/2 mx-auto">
                    <Doughnut
                        data={decisionData}
                        options={{
                            plugins: {
                                tooltip: {
                                    enabled: true,
                                    bodyFont: {
                                        size: 20,
                                    },
                                    titleFont: {
                                        size: 25,
                                    },
                                },
                            },
                        }}
                    />
                </div>
            </div>

            <div className="mb-20">
                <h2 className="text-xl font-semibold mb-5">Trade History Performance</h2>
                <Line data={tradePerformanceData}/>
            </div>
        </div>
    );
};

export default Monitoring;

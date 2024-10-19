import Head from 'next/head';
import Monitoring from './pages/monitoring';

export default function Home() {
    return (
        <div>
            <Head>
                <title>Bitcoin Trading Dashboard</title>
                <meta name="description" content="Monitor your automated Bitcoin trading" />
                <link rel="icon" href="./favicon.ico" />
            </Head>

            <main>
                <Monitoring />
            </main>
        </div>
    )
}
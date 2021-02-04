import React, { Component } from 'react';

class Footer extends Component {
    state = {
        clock:''
    }
    updateClock = () => {
        this.setState ( 
            {
                clock : new Date().toLocaleTimeString()
            }
        )
    }
    componentDidMount() {
        setInterval(this.updateClock, 1000)
    }  
    render() {
        return (
            <div className="fixed-bottom align-items-center">
                <footer className="brick-bg footer-height">
                        <span> 
                        <div className="float-left mt-1 text-gold back-blue mx-3 px-2">2021 A Danware Production</div>
                        <div className="float-right mt-1 text-gold back-blue mx-3 px-2">The local time is {this.state.clock} </div></span>
                </footer>
            </div>
        );
    }
}

export default Footer;
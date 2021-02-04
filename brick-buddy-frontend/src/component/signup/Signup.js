import Axios from 'axios';
import React, { Component } from 'react';

class Signup extends Component {
    state = {
        customer: {
            firstName: '',
            lastName: '',
            blEmail: '',
            blPassword: '',
            email: '',
            passwordOld: '',
            location: '',
        },
        signin: {
            //This name has to match the Java class attribute name for Spring to call the appropriate setter.
            userID: '',
            userPassword: ''
        }
    }
    handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        const tempCustomer = { ...this.state.customer }
        // customer.firstName === customer['firstName']
        tempCustomer[name] = value;
        this.setState(
            {
                customer: tempCustomer
            }
        )
    }
    componentDidMount() {

    }
    handleSubmit = () => {
        // This will continue as an asyncronous call, does not hang the UI waiting for data from backend.
        Axios.post('http://localhost:4500/brickbuddy/registerCustomer', this.state.customer)
            .then(response => {
                let tempSignin = { ...this.state.signin }
                let tempCustomer = { ...this.state.customer }
                tempSignin.userID = response.data.email;
                tempSignin.userPassword = tempCustomer.passwordOld;
                tempCustomer = { ...response.data };
                this.setState(
                    {
                        signin: tempSignin,
                        customer: tempCustomer
                    }
                );
                console.log("About to sign in as ", tempSignin)
                Axios.post('http://localhost:4500/brickbuddy/login', this.state.signin)
                    .then(response => {
                        tempSignin.userPassword = '';
                        this.setState(
                            {
                                signin: tempSignin
                            }
                        )
                        //capture customer info from the user which logged in.
                        const email = response.data.email;
                        localStorage.setItem('loggedInUser', email);
                        //Navigate to home page. 
                        this.props.history.push('/home');
                    }).catch(error => {
                        //Display error message.
                        
                    });
                this.props.history.push('/home');
            }).catch(error => {
                //Display error message.
            });
    }
    render() {
        return (
            <div className="container sign-up-container green-shade head-foot-padding">
                <h2 className="back-blue text-gold">Please sign up below:</h2>
                <form>
                    <div className="row mb-2">
                        <div className="col-md-4">
                            <label for="first">Your First Name</label>
                            <input type="text" onChange={this.handleChange} id="first" name="firstName" value={this.state.customer.firstName} className="form-control" placeholder="First name" aria-label="First name" required />
                        </div>
                        <div className="col-md-4">
                            <label for="last">Your Last Name</label>
                            <input type="text" onChange={this.handleChange} id="last" name="lastName" value={this.state.customer.lastName} className="form-control" placeholder="Last name" aria-label="Last name" required />
                        </div>
                        <div className="col-md-4">
                            <label for="state">State or Territory</label>
                            <select className="custom-select" onChange={this.handleChange} name="location" id="state" placeholder="State/Territory" aria-label="State/Territory" required>
                                <option value="">Select from below...</option>
                                <option value="AK">Alaska</option>
                                <option value="AL">Alabama</option>
                                <option value="AR">Arkansas</option>
                                <option value="AZ">Arizona</option>
                                <option value="CA">California</option>
                                <option value="CO">Colorado</option>
                                <option value="CT">Connecticut</option>
                                <option value="DC">District of Columbia</option>
                                <option value="DE">Delaware</option>
                                <option value="FL">Florida</option>
                                <option value="GA">Georgia</option>
                                <option value="HI">Hawaii</option>
                                <option value="IA">Iowa</option>
                                <option value="ID">Idaho</option>
                                <option value="IL">Illinois</option>
                                <option value="IN">Indiana</option>
                                <option value="KS">Kansas</option>
                                <option value="KY">Kentucky</option>
                                <option value="LA">Louisiana</option>
                                <option value="MA">Massachusetts</option>
                                <option value="MD">Maryland</option>
                                <option value="ME">Maine</option>
                                <option value="MI">Michigan</option>
                                <option value="MN">Minnesota</option>
                                <option value="MO">Missouri</option>
                                <option value="MS">Mississippi</option>
                                <option value="MT">Montana</option>
                                <option value="NC">North Carolina</option>
                                <option value="ND">North Dakota</option>
                                <option value="NE">Nebraska</option>
                                <option value="NH">New Hampshire</option>
                                <option value="NJ">New Jersey</option>
                                <option value="NM">New Mexico</option>
                                <option value="NV">Nevada</option>
                                <option value="NY">New York</option>
                                <option value="OH">Ohio</option>
                                <option value="OK">Oklahoma</option>
                                <option value="OR">Oregon</option>
                                <option value="PA">Pennsylvania</option>
                                <option value="PR">Puerto Rico</option>
                                <option value="RI">Rhode Island</option>
                                <option value="SC">South Carolina</option>
                                <option value="SD">South Dakota</option>
                                <option value="TN">Tennessee</option>
                                <option value="TX">Texas</option>
                                <option value="UT">Utah</option>
                                <option value="VA">Virginia</option>
                                <option value="VT">Vermont</option>
                                <option value="WA">Washington</option>
                                <option value="WI">Wisconsin</option>
                                <option value="WV">West Virginia</option>
                                <option value="WY">Wyoming</option>
                                <option value="AB">Alberta, Canada</option>
                                <option value="BC">British Columbia, Canada</option>
                                <option value="MB">Manitoba, Canada</option>
                                <option value="NB">New Brunswick, Canada</option>
                                <option value="NL">Newfoundland/Labrador, Canada</option>
                                <option value="NT">Northwest Territories, Canada</option>
                                <option value="NS">Nova Scotia, Canada</option>
                                <option value="NU">Nunavut, Canada</option>
                                <option value="OT">Ontario, Canada</option>
                                <option value="PE">Prince Edward Island, Canada</option>
                                <option value="QC">Quebec, Canada</option>
                                <option value="SK">Saskatchewan, Canada</option>
                                <option value="YT">Yukon, Canada</option>
                            </select>
                        </div>
                    </div>
                    {/* <div className="row mb-2">
                        <div className="col-md-7">
                            <label for="blEmail">The Email You Use To Login To Bricklink*</label>   
                            <input type="email" onChange={this.handleChange} id="blEmail" name="blEmail" value={this.state.customer.blEmail} className="form-control" placeholder="Bricklink Email" aria-label="Bricklink Email" />
                        </div>
                        <div className="col-md-5">
                            <label for="blPassword">Your Bricklink Password*</label>
                            <input type="password" onChange={this.handleChange} name="blPassword" value={this.state.customer.blPassword} className="form-control" placeholder="Bricklink Password" aria-label="Bricklink Password" />
                        </div>
                        <small className="ml-4">* Required to automatically get your Bricklink default want list and put selections in your cart.</small>
                    </div> */}
                    <div className="row mb-2">
                        <div className="col-md-7">
                            <label for="email">Your Email Is Your Brick Buddy Username</label>
                            <input type="email" onChange={this.handleChange} id="email" name="email" value={this.state.customer.email} className="form-control" placeholder="Email" aria-label="Email" required />
                        </div>
                        <div className="col-md-5">
                            <label for="password">Your Brick Buddy Password</label>
                            <input type="password" onChange={this.handleChange} id="password" name="passwordOld" value={this.state.customer.passwordOld} className="form-control" placeholder="Password" aria-label="Password" required />
                        </div>
                    </div>
                    <small className="ml-3">The term 'Bricklink' is a trademark of Bricklink, Inc. Brick Buddy is not endorsed or certified by Bricklink, Inc.</small>
                    <div className="d-grid gap-2 mt-2 centered mr-6 pb-3">
                        {/* look at step 35 to find out how to use this with a submit button.  Needs a handler call
                        in the open form tag and something about queries, etc.*/}
                        <button onClick={this.handleSubmit} className="btn btn-block btn-primary back-blue text-gold" type="button">Sign Up</button>
                    </div>
                </form>
            </div>
        );
    }
}

export default Signup;
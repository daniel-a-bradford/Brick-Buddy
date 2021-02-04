import React, { Component } from 'react';
import Axios from 'axios';

class MyPage extends Component {
    state = {
        passwordUpdateStatus: '',
        infoUpdateStatus: '',
        customer: {
            firstName: '',
            lastName: '',
            blEmail: '',
            blPassword: '',
            email: '',
            passwordOld: '',
            passwordNew1: '',
            passwordNew2: '',
            location: ''
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
    handleSubmit = () => {
        // This will continue as an asyncronous call, does not hang the UI waiting for data from backend.
        Axios.post('http://localhost:4500/brickbuddy/updateCustomer', this.state.customer)
            .then(response => {
                //Navigate to thank my-page. 
                let tempCustomer = {...response.data};
                localStorage.setItem('loggedInUser', tempCustomer.email);
                this.setState(
                    {
                        customer: tempCustomer,
                        infoUpdateStatus: <p className="input-valid back-blue mt-4 mx-1 px-1">Success!</p>
                    }
                )
            }).catch(error => {
                if (error.response.status == 409) {
                    this.setState(
                        {
                            infoUpdateStatus: <p className="input-error back-blue mt-4 mx-1 px-1">Email is already used. Please use another.</p>
                        }
                    )
                };
                if (error.response.status == 411) {
                    this.setState(
                        {
                            infoUpdateStatus: <p className="input-error back-blue mt-4 mx-1 px-1">Ensure there are no blank fields.</p>
                        }
                    )
                };
            });
    }
    handlePassword = () => {
        // This will continue as an asyncronous call, does not hang the UI waiting for data from backend.
        Axios.post('http://localhost:4500/brickbuddy/updatePassword', this.state.customer)
            .then(response => {
                let tempCustomer = {...this.state.customer};
                tempCustomer.passwordOld = '';
                tempCustomer.passwordNew1 = '';
                tempCustomer.passwordNew2 = '';
                this.setState(
                    {
                        customer: tempCustomer,
                        passwordUpdateStatus: <p className="input-valid back-blue mb-2 mx-1 px-1">Success!</p>
                    }
                )
            }).catch(error => {
                if (error.response.status == 411) {
                    this.setState(
                        {
                            passwordUpdateStatus: <p className="input-error back-blue mb-2 mx-1 px-1">Please enter all values.</p>
                        }
                    )
                };
                if (error.response.status == 422) {
                    this.setState(
                        {
                            passwordUpdateStatus: <p className="input-error back-blue mb-2 mx-1 px-1">New passwords do not match.</p>
                        }
                    )
                };
                if (error.response.status == 401) {
                    this.setState(
                        {
                            passwordUpdateStatus: <p className="input-error back-blue mb-2 mx-1 px-1">Old password incorrect.</p>
                        }
                    )
                };
            });
    }
    componentDidMount() {
        const email = localStorage.getItem("loggedInUser");
        const params = { email };
        // {}
        Axios.get('http://localhost:4500/brickbuddy/findCustomerByEmail', { params })
            .then(response => {
                this.setState(
                    {
                        customer: response.data
                    }
                )
            })
    }
    render() {
        return (
            <div className="fullscreen bigjumble-bg py-5">
                <div className="container">
                    <div className="row green-shade py-3">
                        <div className="col-md-8 order-md-1">
                            <h2 className="back-blue text-gold">Please update your information below: </h2>
                            <form>
                                <div className="row mb-2">
                                    <div className="col-md-4">
                                        <label for="first">Your First Name</label>
                                        <input type="text" onChange={this.handleChange} id="first" name="firstName"
                                            value={this.state.customer.firstName} className="form-control" placeholder="First name"
                                            aria-label="First name" required />
                                    </div>
                                    <div className="col-md-4">
                                        <label for="last">Your Last Name</label>
                                        <input type="text" onChange={this.handleChange} id="last" name="lastName"
                                            value={this.state.customer.lastName} className="form-control" placeholder="Last name"
                                            aria-label="Last name" required />
                                    </div>
                                    <div className="col-md-4">
                                        <label for="state">State or Territory</label>
                                        <select className="custom-select" onChange={this.handleChange} name="location" id="state"
                                            value={this.state.customer.location}
                                            aria-label="State/Territory" required>
                                            <option value="">{this.state.customer.location} </option>
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
                                {/*                             <div className="row mb-2">
                                <div className="col-md-6">
                                    <label for="blEmail">The Email You Use To Login To Bricklink*</label>
                                    <input type="email" onChange={this.handleChange} id="blEmail" name="blEmail" 
                                    value={this.state.customer.blEmail} className="form-control" placeholder="Bricklink Email" 
                                    aria-label="Bricklink Email" />
                                </div>
                                <div className="col-md-6">
                                    <label for="blPassword">Your Bricklink Password*</label>
                                    <input type="password" onChange={this.handleChange} name="blPassword" 
                                    value={this.state.customer.blPassword} className="form-control" placeholder="Bricklink Password" 
                                    aria-label="Bricklink Password" />
                                </div>
                                <small className="text-muted ml-4">* Required to automatically get your Bricklink default want list and put selections in your cart.</small>
                            </div> */}
                                <div className="row mb-2">
                                    <div className="col-md-6">
                                        <label for="email">Your Email Is Your Brick Buddy Username</label>
                                        <input type="email" onChange={this.handleChange} id="email" name="email"
                                            value={this.state.customer.email} className="form-control" placeholder="Email"
                                            aria-label="Email" required />
                                    </div>
                                    <div>{this.state.infoUpdateStatus}</div>
                                </div>
                                <small className="ml-3">The term 'Bricklink' is a trademark of Bricklink, Inc. Brick Buddy is not endorsed or certified by Bricklink, Inc."</small>
                                <div className="d-grid gap-2 mt-2 centered mr-6 ">
                                    {/* look at step 35 to find out how to use this with a submit button.  Needs a handler call
                        in the open form tag and something about queries, etc.*/}
                                    <button onClick={this.handleSubmit} className="btn btn-block btn-primary back-blue text-gold" type="button">Update My Information</button>
                                </div>
                            </form>
                        </div>
                        <div className="col-md-4 order-md-2">
                            <h4 className="mb-3 back-blue text-gold">Set new password</h4>
                            <form className="needs-validation">
                                <div className="col-md-12">
                                    <label for="passwordOld">Current password</label>
                                    <input type="password" onChange={this.handleChange} name="passwordOld" className="form-control"
                                        id="passwordOld" placeholder="Current password" required />
                                    <div className="invalid-feedback">Enter a valid password.</div>
                                </div>
                                <div className="col-md-12">
                                    <label for="passwordNew1">New password</label>
                                    <input type="password" onChange={this.handleChange} name="passwordNew1" className="form-control"
                                        id="passwordNew1" placeholder="Enter new password" required />
                                    <div className="invalid-feedback">Enter a valid password.</div>
                                </div>
                                <div className="col-md-12 pb-3">
                                    <label for="passwordNew2">Confirm new password</label>
                                    <input type="password" onChange={this.handleChange} name="passwordNew2" className="form-control"
                                        id="passwordNew2" placeholder="Confirm new password" required />
                                    <div className="invalid-feedback">Enter a valid password.</div>
                                </div>
                                {this.state.passwordUpdateStatus}
                                <button onClick={this.handlePassword} className="btn btn-block btn-primary back-blue text-gold" type="button">Update password</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default MyPage;